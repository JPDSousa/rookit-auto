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
package org.rookit.auto.javax.runtime.element;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class RuntimeElement implements Element {

    private final Name simpleName;
    private final TypeMirror typeMirror;
    private final ElementKind elementKind;
    private final Set<Modifier> modifiers;
    private final Element enclosingElement;
    private final List<? extends Element> enclosedElements;
    private final AnnotatedConstruct annotatedConstruct;

    RuntimeElement(
            final Name simpleName,
            final TypeMirror typeMirror,
            final ElementKind elementKind,
            final Collection<Modifier> modifiers,
            final Element enclosingElement,
            final Collection<? extends Element> enclosedElements,
            final AnnotatedConstruct annotatedConstruct) {
        this.simpleName = simpleName;
        this.typeMirror = typeMirror;
        this.elementKind = elementKind;
        this.modifiers = ImmutableSet.copyOf(modifiers);
        this.enclosingElement = enclosingElement;
        this.enclosedElements = ImmutableList.copyOf(enclosedElements);
        this.annotatedConstruct = annotatedConstruct;
    }

    @Override
    public TypeMirror asType() {
        return this.typeMirror;
    }

    @Override
    public ElementKind getKind() {
        return this.elementKind;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return this.modifiers;
    }

    @Override
    public Name getSimpleName() {
        return this.simpleName;
    }

    @Override
    public Element getEnclosingElement() {
        return this.enclosingElement;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return this.enclosedElements;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return this.annotatedConstruct.getAnnotationMirrors();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        return this.annotatedConstruct.getAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        return this.annotatedConstruct.getAnnotationsByType(annotationType);
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        return v.visitUnknown(this, p);
    }

    @Override
    public String toString() {
        return "RuntimeElement{" +
                "simpleName=" + this.simpleName +
                ", typeMirror=" + this.typeMirror +
                ", elementKind=" + this.elementKind +
                ", modifiers=" + this.modifiers +
                ", enclosingElement=" + this.enclosingElement +
                ", enclosedElements=" + this.enclosedElements +
                ", annotatedConstruct=" + this.annotatedConstruct +
                "}";
    }

}
