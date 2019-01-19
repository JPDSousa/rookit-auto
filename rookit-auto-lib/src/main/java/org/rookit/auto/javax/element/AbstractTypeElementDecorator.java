/*******************************************************************************
 * Copyright (C) 2018 Joao Sousa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.rookit.auto.javax.element;

import com.google.common.collect.ImmutableList;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.ExtendedTypeMirror;
import org.rookit.auto.javax.ExtendedTypeMirrorFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.naming.PackageReference;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.convention.annotation.Entity;
import org.rookit.convention.annotation.EntityExtension;
import org.rookit.convention.annotation.PartialEntity;
import org.rookit.convention.annotation.PropertyContainer;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractTypeElementDecorator implements ExtendedTypeElement {

    private final TypeElement delegate;
    private final ElementUtils utils;
    private final OptionalFactory optionalFactory;
    private final PackageReferenceFactory packageReferenceFactory;
    private final Collection<ExtendedProperty> properties;
    private final PropertyExtractor extractor;
    private final ExtendedTypeMirrorFactory factory;

    AbstractTypeElementDecorator(final TypeElement delegate,
                                 final ElementUtils utils,
                                 final OptionalFactory optionalFactory,
                                 final PackageReferenceFactory packageFactory,
                                 final Collection<ExtendedProperty> properties,
                                 final PropertyExtractor extractor,
                                 final ExtendedTypeMirrorFactory factory) {
        this.delegate = delegate;
        this.utils = utils;
        this.optionalFactory = optionalFactory;
        this.packageReferenceFactory = packageFactory;
        this.properties = ImmutableList.copyOf(properties);
        this.extractor = extractor;
        this.factory = factory;
    }



    final OptionalFactory optionalFactory() {
        return this.optionalFactory;
    }

    private StreamEx<TypeElement> conventionTypeInterfaces() {
        // TODO cache this
        return StreamEx.of(getInterfaces())
                .map(this.factory::create)
                .map(ExtendedTypeMirror::toElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .select(TypeElement.class)
                .filter(this.utils::isConventionElement);
    }

    @Override
    public PackageReference packageInfo() {
        // TODO cache this
        Element enclosingElement = getEnclosingElement();
        while(enclosingElement.getKind() != ElementKind.PACKAGE) {
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        return this.packageReferenceFactory.create((PackageElement) enclosingElement);
    }

    @Override
    public Collection<ExtendedProperty> properties() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType as already immutable
        return this.properties;
    }

    @Override
    public boolean isTopLevel() {
        // TODO cache this
        return !conventionTypeInterfaces()
                .findFirst()
                .isPresent();
    }

    @Override
    public StreamEx<ExtendedTypeElement> conventionInterfaces() {
        // TODO cache this
        return conventionTypeInterfaces()
                .map(this::createParent);
    }

    private ExtendedTypeElement createParent(final TypeElement element) {
        final Collection<ExtendedProperty> properties = this.extractor.fromType(element)
                .collect(Collectors.toList());
        return new ParentTypeElementDecorator(element, this, this.utils,
                this.optionalFactory, this.packageReferenceFactory, properties, this.extractor, factory);
    }

    @Override
    public boolean isEntity() {
        // TODO cache this
        return Objects.nonNull(this.getAnnotation(Entity.class))
                || isEntityExtension();
    }

    @Override
    public boolean isPartialEntity() {
        // TODO cache this
        return Objects.nonNull(getAnnotation(PartialEntity.class));
    }

    @Override
    public boolean isEntityExtension() {
        // TODO cache this
        return Objects.nonNull(getAnnotation(EntityExtension.class));
    }

    @Override
    public boolean isPropertyContainer() {
        // TODO cache this
        return Objects.nonNull(getAnnotation(PropertyContainer.class));
    }

    @Override
    public Optional<ExtendedTypeElement> upstreamEntity() {
        // TODO cache this
        if (isTopLevel()) {
            return this.optionalFactory.empty();
        }
        if (isEntityExtension()) {
            return this.optionalFactory.fromJavaOptional(conventionInterfaces()
                    .map(ExtendedTypeElement::upstreamEntity)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst());
        }
        if (isEntity()) {
            return this.optionalFactory.of(this);
        }

        return this.optionalFactory.empty();
    }

    @Override
    public List<? extends ExtendedTypeMirror> getInterfaces() {
        // TODO cache this
        return StreamEx.of(this.delegate.getInterfaces())
                .map(this.factory::create)
                .toImmutableList();
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return this.delegate.getEnclosedElements();
    }

    @Override
    public NestingKind getNestingKind() {
        return this.delegate.getNestingKind();
    }

    @Override
    public Name getQualifiedName() {
        return this.delegate.getQualifiedName();
    }

    @Override
    public Name getSimpleName() {
        return this.delegate.getSimpleName();
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return this.delegate.getTypeParameters();
    }

    @Override
    public Element getEnclosingElement() {
        return this.delegate.getEnclosingElement();
    }

    @Override
    public ExtendedTypeMirror getSuperclass() {
        return this.factory.create(this.delegate.getSuperclass());
    }

    @Override
    public ExtendedTypeMirror asType() {
        return this.factory.create(this.delegate.asType());
    }

    @Override
    public ElementKind getKind() {
        return this.delegate.getKind();
    }

    @Override
    public Set<Modifier> getModifiers() {
        return this.delegate.getModifiers();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return this.delegate.getAnnotationMirrors();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        return this.delegate.getAnnotation(annotationType);
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        return this.delegate.accept(v, p);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        return this.delegate.getAnnotationsByType(annotationType);
    }

    @Override
    public String toString() {
        return "AbstractTypeElementDecorator{" +
                "delegate=" + this.delegate +
                ", utils=" + this.utils +
                ", optionalFactory=" + this.optionalFactory +
                ", packageReferenceFactory=" + this.packageReferenceFactory +
                ", properties=" + this.properties +
                ", extractor=" + this.extractor +
                ", factory=" + this.factory +
                "}";
    }
}
