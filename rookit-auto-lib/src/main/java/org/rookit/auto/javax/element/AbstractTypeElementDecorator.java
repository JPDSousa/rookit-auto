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
import one.util.streamex.StreamEx;
import org.rookit.utils.convention.annotation.Entity;
import org.rookit.utils.convention.annotation.EntityExtension;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

abstract class AbstractTypeElementDecorator implements ExtendedTypeElement {

    private final TypeElement delegate;
    private final ElementUtils utils;

    AbstractTypeElementDecorator(final TypeElement delegate, final ElementUtils utils) {
        this.delegate = delegate;
        this.utils = utils;
    }

    @Override
    public StreamEx<ExtendedTypeElement> conventionInterfaces() {
        return StreamEx.of(getInterfaces())
                .map(this.utils::toElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .select(TypeElement.class)
                .filter(this.utils::isConventionElement)
                .map(element -> new ParentTypeElementDecorator(element, this, this.utils));
    }

    @Override
    public boolean isEntity() {
        return Objects.nonNull(this.getAnnotation(Entity.class))
                || Objects.nonNull(this.getAnnotation(EntityExtension.class));
    }

    @Override
    public boolean isTopLevel() {
        return StreamEx.of(getInterfaces())
                .map(this.utils::toElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .select(TypeElement.class)
                .noneMatch(this.utils::isConventionElement);
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return this.delegate.getEnclosedElements();
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
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        return this.delegate.getAnnotationsByType(annotationType);
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        return this.delegate.accept(v, p);
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
    public TypeMirror asType() {
        return this.delegate.asType();
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
    public Name getSimpleName() {
        return this.delegate.getSimpleName();
    }

    @Override
    public TypeMirror getSuperclass() {
        return this.delegate.getSuperclass();
    }

    @Override
    public List<? extends TypeMirror> getInterfaces() {
        return this.delegate.getInterfaces();
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("delegate", this.delegate)
                .add("utils", this.utils)
                .toString();
    }
}
