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
package org.rookit.auto.javax.runtime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

final class RuntimePackageElement implements PackageElement {

    private final Name qualifiedName;
    private final Name simpleName;
    private final TypeMirror typeMirror;

    RuntimePackageElement(final Name qualifiedName,
                          final Name simpleName,
                          final TypeMirror typeMirror) {
        this.qualifiedName = qualifiedName;
        this.simpleName = simpleName;
        this.typeMirror = typeMirror;
    }

    @Override
    public Name getQualifiedName() {
        return this.qualifiedName;
    }

    @Override
    public TypeMirror asType() {
        return this.typeMirror;
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.PACKAGE;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return ImmutableSet.of();
    }

    @Override
    public Name getSimpleName() {
        return this.simpleName;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        // TODO improve me -> actually calculate the enclosed elements
        return ImmutableList.of();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        // TODO improve me -> actually calculate the annotations
        return ImmutableList.of();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        // TODO sad to return null here, but since we have no way of calculating the annotations...
        return null;
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        // TODO again, to be changed when we're actually able to calculate annotations
        return (A[]) new Object[0];
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        return v.visitPackage(this, p);
    }

    @Override
    public boolean isUnnamed() {
        // Always false, given that this implementation forces a name
        return false;
    }

    @Override
    public Element getEnclosingElement() {
        // null is intentional. Check javadocs
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if ((o == null) || (getClass() != o.getClass())) return false;

        final RuntimePackageElement other = (RuntimePackageElement) o;

        return new EqualsBuilder()
                .append(this.qualifiedName, other.qualifiedName)
                .append(this.simpleName, other.simpleName)
                .append(this.typeMirror, other.typeMirror)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.qualifiedName)
                .append(this.simpleName)
                .append(this.typeMirror)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RuntimePackageElement{" +
                "qualifiedName=" + this.qualifiedName +
                ", simpleName=" + this.simpleName +
                ", typeMirror=" + this.typeMirror +
                "}";
    }
}
