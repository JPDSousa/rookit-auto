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
package org.rookit.auto.javax.type;

import com.google.common.collect.ImmutableSet;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.pack.PackageReference;
import org.rookit.utils.optional.Optional;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeParameterElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class PropertyOverridingExtendedTypeElement implements ExtendedTypeElement {

    private final ExtendedTypeElement delegate;
    private final Collection<Property> overriddenProperties;

    PropertyOverridingExtendedTypeElement(final ExtendedTypeElement delegate,
                                          final Collection<Property> overriddenProperties) {
        this.delegate = delegate;
        this.overriddenProperties = ImmutableSet.copyOf(overriddenProperties);
    }

    @Override
    public Optional<ExtendedTypeElement> child() {
        return this.delegate.child();
    }

    @Override
    public boolean isTopLevel() {
        return this.delegate.isTopLevel();
    }

    @Override
    public boolean isEntity() {
        return this.delegate.isEntity();
    }

    @Override
    public boolean isPartialEntity() {
        return this.delegate.isPartialEntity();
    }

    @Override
    public boolean isEntityExtension() {
        return this.delegate.isEntityExtension();
    }

    @Override
    public boolean isPropertyContainer() {
        return this.delegate.isPropertyContainer();
    }

    @Override
    public boolean isConvention() {
        return this.delegate.isConvention();
    }

    @Override
    public Optional<ExtendedTypeElement> upstreamEntity() {
        return this.delegate.upstreamEntity();
    }

    @Override
    public StreamEx<ExtendedTypeElement> conventionInterfaces() {
        return this.delegate.conventionInterfaces();
    }

    @Override
    public PackageReference packageInfo() {
        return this.delegate.packageInfo();
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType") // already immutable
    @Override
    public Collection<Property> properties() {
        return this.overriddenProperties;
    }

    @Override
    public List<? extends ExtendedTypeMirror> getInterfaces() {
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
    public Name getSimpleName() {
        return this.delegate.getSimpleName();
    }

    @Override
    public ExtendedTypeMirror getSuperclass() {
        return this.delegate.getSuperclass();
    }

    @Override
    public ExtendedTypeMirror asType() {
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
    public String toString() {
        return "PropertyOverridingExtendedTypeElement{" +
                "delegate=" + this.delegate +
                ", overriddenProperties=" + this.overriddenProperties +
                "}";
    }
}
