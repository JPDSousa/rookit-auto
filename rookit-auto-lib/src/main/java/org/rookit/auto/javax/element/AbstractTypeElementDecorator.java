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

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

abstract class AbstractTypeElementDecorator extends DelegateTypeElement implements ExtendedTypeElement {

    private final ElementUtils utils;
    private final OptionalFactory optionalFactory;
    private final PackageReferenceFactory packageReferenceFactory;
    private final Collection<ExtendedProperty> properties;
    private final PropertyExtractor extractor;

    AbstractTypeElementDecorator(final TypeElement delegate,
                                 final ElementUtils utils,
                                 final OptionalFactory optionalFactory,
                                 final PackageReferenceFactory packageFactory,
                                 final Collection<ExtendedProperty> properties,
                                 final PropertyExtractor extractor) {
        super(delegate);
        this.utils = utils;
        this.optionalFactory = optionalFactory;
        this.packageReferenceFactory = packageFactory;
        this.properties = ImmutableList.copyOf(properties);
        this.extractor = extractor;
    }

    final OptionalFactory optionalFactory() {
        return this.optionalFactory;
    }

    private StreamEx<TypeElement> conventionTypeInterfaces() {
        return StreamEx.of(getInterfaces())
                .map(this.utils::toElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .select(TypeElement.class)
                .filter(this.utils::isConventionElement);
    }

    @Override
    public PackageReference packageInfo() {
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
        return !conventionTypeInterfaces()
                .findFirst()
                .isPresent();
    }

    @Override
    public StreamEx<ExtendedTypeElement> conventionInterfaces() {
        return conventionTypeInterfaces()
                .map(this::createParent);
    }

    private ExtendedTypeElement createParent(final TypeElement element) {
        final Collection<ExtendedProperty> properties = this.extractor.fromType(element)
                .collect(Collectors.toList());
        return new ParentTypeElementDecorator(element, this, this.utils,
                this.optionalFactory, this.packageReferenceFactory, properties, this.extractor);
    }

    @Override
    public boolean isEntity() {
        return Objects.nonNull(this.getAnnotation(Entity.class))
                || isEntityExtension();
    }

    @Override
    public boolean isPartialEntity() {
        return Objects.nonNull(getAnnotation(PartialEntity.class));
    }

    @Override
    public boolean isEntityExtension() {
        return Objects.nonNull(getAnnotation(EntityExtension.class));
    }

    @Override
    public boolean isPropertyContainer() {
        return Objects.nonNull(getAnnotation(PropertyContainer.class));
    }

    @Override
    public Optional<ExtendedTypeElement> upstreamEntity() {
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
    public String toString() {
        return "AbstractTypeElementDecorator{" +
                "utils=" + this.utils +
                ", optionalFactory=" + this.optionalFactory +
                ", packageReferenceFactory=" + this.packageReferenceFactory +
                ", properties=" + this.properties +
                ", extractor=" + this.extractor +
                "} " + super.toString();
    }
}
