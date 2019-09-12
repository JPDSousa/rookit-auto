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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

final class RuntimeTypeMirror implements TypeMirror {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(RuntimeTypeMirror.class);

    private final Class<?> clazz;
    private final List<? extends AnnotationMirror> mirrors;
    private final TypeKind kind;

    RuntimeTypeMirror(
            final Class<?> clazz,
            final Collection<? extends AnnotationMirror> mirrors,
            final TypeKind kind) {
        this.clazz = clazz;
        this.mirrors = ImmutableList.copyOf(mirrors);
        this.kind = kind;
    }

    @Override
    public TypeKind getKind() {
        return this.kind;
    }

    @Override
    public <R, P> R accept(final TypeVisitor<R, P> v, final P p) {
        logger.warn("Cannot determine which method to visit, since this is a generic implementation to TypeMirror");
        return v.visitUnknown(this, p);
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return this.mirrors;
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        logger.trace("Delegating call to '{}'", this.clazz);
        return this.clazz.getAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        logger.trace("Delegating call to '{}'", this.clazz);
        return this.clazz.getAnnotationsByType(annotationType);
    }

    @Override
    public String toString() {
        return "RuntimeTypeMirror{" +
                "clazz=" + this.clazz +
                ", mirrors=" + this.mirrors +
                ", kind=" + this.kind +
                "}";
    }

}
