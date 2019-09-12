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
package org.rookit.auto.javax.runtime.mirror;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

final class RuntimeDeclaredType implements DeclaredType {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(RuntimeDeclaredType.class);

    private final TypeMirror typeMirror;
    private final Element element;
    private final TypeMirror enclosingType;
    private final List<? extends TypeMirror> typeArguments;

    RuntimeDeclaredType(
            final TypeMirror typeMirror,
            final Element element,
            final TypeMirror enclosingType,
            final Collection<? extends TypeMirror> typeArguments) {
        this.typeMirror = typeMirror;
        this.element = element;
        this.enclosingType = enclosingType;
        this.typeArguments = ImmutableList.copyOf(typeArguments);
    }

    @Override
    public Element asElement() {
        return this.element;
    }

    @Override
    public TypeMirror getEnclosingType() {
        return this.enclosingType;
    }

    @Override
    public List<? extends TypeMirror> getTypeArguments() {
        return this.typeArguments;
    }

    @Override
    public TypeKind getKind() {
        logger.trace("Delegating call to typeMirror");
        return this.typeMirror.getKind();
    }

    @Override
    public <R, P> R accept(final TypeVisitor<R, P> v, final P p) {
        return v.visitDeclared(this, p);
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        logger.trace("Delegating call to typeMirror");
        return this.typeMirror.getAnnotationMirrors();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        logger.trace("Delegating call to typeMirror");
        return this.typeMirror.getAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        logger.trace("Delegating call to typeMirror");
        return this.typeMirror.getAnnotationsByType(annotationType);
    }

    @Override
    public String toString() {
        return "RuntimeDeclaredType{" +
                "typeMirror=" + this.typeMirror +
                ", element=" + this.element +
                ", enclosingType=" + this.enclosingType +
                ", typeArguments=" + this.typeArguments +
                "}";
    }

}
