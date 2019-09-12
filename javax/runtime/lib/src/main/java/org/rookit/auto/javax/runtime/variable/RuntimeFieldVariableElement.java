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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class RuntimeFieldVariableElement implements VariableElement {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(RuntimeFieldVariableElement.class);

    private final AnnotatedConstruct annotatedConstruct;
    private final Element enclosingElement;
    private final Set<Modifier> modifiers;
    private final Name fieldName;
    private final TypeMirror typeMirror;

    RuntimeFieldVariableElement(
            final AnnotatedConstruct annotatedConstruct,
            final Element enclosingElement,
            final Collection<Modifier> modifiers,
            final Name fieldName,
            final TypeMirror typeMirror) {
        this.annotatedConstruct = annotatedConstruct;
        this.enclosingElement = enclosingElement;
        this.modifiers = ImmutableSet.copyOf(modifiers);
        this.fieldName = fieldName;
        this.typeMirror = typeMirror;
    }

    @Override
    public Object getConstantValue() {
        logger.warn("It is not possible to fetch a default initialization value for a field through reflection");
        return null;
    }

    @Override
    public TypeMirror asType() {
        return this.typeMirror;
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.FIELD;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return this.modifiers;
    }

    @Override
    public Name getSimpleName() {
        return this.fieldName;
    }

    @Override
    public Element getEnclosingElement() {
        return this.enclosingElement;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        // fields have no enclosed elements
        return ImmutableList.of();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        logger.trace("Delegating call to annotatedConstruct");
        return this.annotatedConstruct.getAnnotationMirrors();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        logger.trace("Delegating call to annotatedConstruct");
        return this.annotatedConstruct.getAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        logger.trace("Delegating call to annotatedConstruct");
        return this.annotatedConstruct.getAnnotationsByType(annotationType);
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        logger.trace("Visiting variable fieldTypeElement");
        return v.visitVariable(this, p);
    }

    @Override
    public String toString() {
        return "RuntimeFieldVariableElement{" +
                "annotatedConstruct=" + this.annotatedConstruct +
                ", enclosingElement=" + this.enclosingElement +
                ", modifiers=" + this.modifiers +
                ", fieldName=" + this.fieldName +
                ", typeMirror=" + this.typeMirror +
                "}";
    }

}
