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
package org.rookit.auto.javax.runtime.entity;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;

final class ClassEntity implements RuntimeEntity {

    private final Class<?> clazz;
    private final ElementKind kind;

    ClassEntity(final Class<?> clazz, final ElementKind kind) {
        this.clazz = clazz;
        this.kind = kind;
    }

    @Override
    public int modifiers() {
        return this.clazz.getModifiers();
    }

    @Override
    public String name() {
        return this.clazz.getName();
    }

    @Override
    public ElementKind kind() {
        return this.kind;
    }

    @Override
    public <R, P> R accept(final RuntimeEntityVisitor<R, P> visitor, final P parameter) {
        return visitor.visitClass(this.clazz, parameter);
    }

    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
        return this.clazz.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return this.clazz.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return this.clazz.getDeclaredAnnotations();
    }

    @Override
    public String toString() {
        return "ClassEntity{" +
                "clazz=" + this.clazz +
                ", kind=" + this.kind +
                "}";
    }

}
