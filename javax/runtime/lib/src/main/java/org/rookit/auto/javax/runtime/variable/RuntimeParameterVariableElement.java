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

final class RuntimeParameterVariableElement implements VariableElement {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(RuntimeParameterVariableElement.class);

    private final AnnotatedConstruct annotatedConstruct;
    private final TypeMirror typeMirror;
    private final Set<Modifier> modifiers;
    private final Name simpleName;
    private final Element enclosingElement;

    RuntimeParameterVariableElement(
            final AnnotatedConstruct annotatedConstruct,
            final TypeMirror typeMirror,
            final Collection<Modifier> modifiers,
            final Name simpleName,
            final Element enclosingElement) {
        this.annotatedConstruct = annotatedConstruct;
        this.typeMirror = typeMirror;
        this.modifiers = ImmutableSet.copyOf(modifiers);
        this.simpleName = simpleName;
        this.enclosingElement = enclosingElement;
    }

    @Override
    public Object getConstantValue() {
        // parameters have no default values
        return null;
    }

    @Override
    public TypeMirror asType() {
        return this.typeMirror;
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.PARAMETER;
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
        // parameters have no enclosed elements
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
        return v.visitVariable(this, p);
    }

    @Override
    public String toString() {
        return "RuntimeParameterVariableElement{" +
                "annotatedConstruct=" + this.annotatedConstruct +
                ", typeMirror=" + this.typeMirror +
                ", modifiers=" + this.modifiers +
                ", simpleName=" + this.simpleName +
                ", enclosingElement=" + this.enclosingElement +
                "}";
    }

}
