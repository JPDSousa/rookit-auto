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

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.rookit.utils.convention.annotation.Entity;
import org.rookit.utils.convention.annotation.EntityExtension;
import org.rookit.utils.convention.annotation.PartialEntity;
import org.rookit.utils.convention.annotation.PropertyContainer;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

final class ElementUtilsImpl implements ElementUtils {

    private final Elements elements;
    private final Types types;
    private final TypeParameterExtractor extractor;
    private final Collection<Class<? extends Annotation>> annotations;

    @Inject
    private ElementUtilsImpl(final Elements elements, final Types types, final TypeParameterExtractor extractor) {
        this.elements = elements;
        this.types = types;
        this.extractor = extractor;
        this.annotations = ImmutableSet.of(Entity.class,
                EntityExtension.class,
                PartialEntity.class,
                org.rookit.utils.convention.annotation.Property.class,
                PropertyContainer.class);
    }

    @Override
    public boolean isSameType(final TypeMirror type, final TypeMirror anotherType) {
        return this.types.isSameType(type, anotherType);
    }

    @Override
    public TypeMirror erasure(final Class<?> clazz) {
        return this.types.erasure(this.elements.getTypeElement(clazz.getCanonicalName()).asType());
    }

    @Override
    public boolean isSameTypeErasure(final TypeMirror type, final TypeMirror anotherType) {
        final TypeMirror typeErasure = this.types.erasure(type);
        final TypeMirror anotherTypeErasure = this.types.erasure(anotherType);
        return isSameType(typeErasure, anotherTypeErasure);
    }

    @Override
    public Collection<? extends TypeMirror> typeParameters(final TypeMirror type) {
        return this.extractor.extract(type);
    }

    @Override
    public TypeMirror primitive(final TypeKind typeKind) {
        return this.types.getPrimitiveType(typeKind);
    }

    @Override
    public Optional<Element> toElement(final TypeMirror typeMirror) {
        return Optional.ofNullable(this.types.asElement(typeMirror));
    }

    @Override
    public boolean isConventionElement(final AnnotatedConstruct element) {
        for (final Class<? extends Annotation> annotation : this.annotations) {
            if (Objects.nonNull(element.getAnnotation(annotation))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ExtendedTypeElement extend(final TypeElement baseElement) {
        return new TypeElementDecoratorImpl(baseElement, this);
    }

    @Override
    public PackageElement packageOf(final Element element) {
        if (element instanceof PackageElement) {
            return (PackageElement) element;
        }
        final Element enclosing = element.getEnclosingElement();
        if (Objects.isNull(enclosing)) {
            throw new IllegalArgumentException("Cannot extract package from: " + element);
        }
        return packageOf(enclosing);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("elements", this.elements)
                .add("types", this.types)
                .add("extractor", this.extractor)
                .add("annotations", this.annotations)
                .toString();
    }
}
