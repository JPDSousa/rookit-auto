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
package org.rookit.auto.javax.runtime.type.parameter;

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
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class RuntimeTypeParameterElement implements TypeParameterElement {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(RuntimeTypeParameterElement.class);

    private final TypeMirror typeMirror;
    private final AnnotatedConstruct annotatedConstruct;
    private final Element genericElement;
    private final List<? extends TypeMirror> bounds;
    private final Name simpleName;

    RuntimeTypeParameterElement(
            final TypeMirror typeMirror,
            final AnnotatedConstruct annotatedConstruct,
            final Element genericElement,
            final Collection<? extends TypeMirror> bounds,
            final Name simpleName) {
        this.typeMirror = typeMirror;
        this.annotatedConstruct = annotatedConstruct;
        this.genericElement = genericElement;
        this.bounds = ImmutableList.copyOf(bounds);
        this.simpleName = simpleName;
    }

    @Override
    public Element getGenericElement() {
        return this.genericElement;
    }

    @Override
    public List<? extends TypeMirror> getBounds() {
        return this.bounds;
    }

    @Override
    public TypeMirror asType() {
        return this.typeMirror;
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.TYPE_PARAMETER;
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
    public Element getEnclosingElement() {
        return getGenericElement();
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
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
        logger.trace("Visiting type parameter");
        return v.visitTypeParameter(this, p);
    }

    @Override
    public String toString() {
        return "RuntimeTypeParameterElement{" +
                "typeMirror=" + this.typeMirror +
                ", annotatedConstruct=" + this.annotatedConstruct +
                ", genericElement=" + this.genericElement +
                ", bounds=" + this.bounds +
                ", simpleName=" + this.simpleName +
                "}";
    }

}
