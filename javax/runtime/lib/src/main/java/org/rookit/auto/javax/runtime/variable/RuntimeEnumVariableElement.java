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
package org.rookit.auto.javax.runtime.variable;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

final class RuntimeEnumVariableElement implements VariableElement {

    private final Element enumElement;
    private final Enum<?> enumValue;

    RuntimeEnumVariableElement(
            final Element enumElement,
            final Enum<?> enumValue) {
        this.enumElement = enumElement;
        this.enumValue = enumValue;
    }

    @Override
    public Object getConstantValue() {
        // TODO not sure about this
        return this.enumValue;
    }

    @Override
    public TypeMirror asType() {
        return this.enumElement.asType();
    }

    @Override
    public ElementKind getKind() {
        return this.enumElement.getKind();
    }

    @Override
    public Set<Modifier> getModifiers() {
        return this.enumElement.getModifiers();
    }

    @Override
    public Name getSimpleName() {
        return this.enumElement.getSimpleName();
    }

    @Override
    public Element getEnclosingElement() {
        return this.enumElement.getEnclosingElement();
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return this.enumElement.getEnclosedElements();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return this.enumElement.getAnnotationMirrors();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        return this.enumElement.getAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        return this.enumElement.getAnnotationsByType(annotationType);
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        return v.visitVariable(this, p);
    }

    @Override
    public String toString() {
        return "RuntimeEnumVariableElement{" +
                "enumElement=" + this.enumElement +
                ", enumValue=" + this.enumValue +
                "}";
    }

}
