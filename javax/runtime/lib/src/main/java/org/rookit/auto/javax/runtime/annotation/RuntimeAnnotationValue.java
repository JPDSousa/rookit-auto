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
package org.rookit.auto.javax.runtime.annotation;

import com.google.common.collect.ImmutableList;
import org.rookit.auto.javax.runtime.RuntimeTypeMirrorFactory;
import org.rookit.auto.javax.runtime.RuntimeVariableElementFactory;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

final class RuntimeAnnotationValue implements AnnotationValue {

    private final RuntimeTypeMirrorFactory typeMirrorFactory;
    private final RuntimeVariableElementFactory variableElementFactory;
    private final RuntimeAnnotationMirrorFactory annotationFactory;
    private final Object value;

    RuntimeAnnotationValue(
            final RuntimeTypeMirrorFactory typeMirrorFactory,
            final RuntimeVariableElementFactory variableFactory,
            final RuntimeAnnotationMirrorFactory annotationFactory,
            final Object value) {
        this.typeMirrorFactory = typeMirrorFactory;
        this.variableElementFactory = variableFactory;
        this.annotationFactory = annotationFactory;
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @SuppressWarnings("AutoUnboxing")
    @Override
    public <R, P> R accept(final AnnotationValueVisitor<R, P> v, final P p) {
        if (this.value instanceof Boolean) {
            return v.visitBoolean((Boolean) this.value, p);
        } else if (this.value instanceof Byte) {
            return v.visitByte((Byte) this.value, p);
        } else if (this.value instanceof Character) {
            return v.visitChar((Character) this.value, p);
        } else if (this.value instanceof Double) {
            return v.visitDouble((Double) this.value, p);
        } else if (this.value instanceof Float) {
            return v.visitFloat((Float) this.value, p);
        } else if (this.value instanceof Integer) {
            return v.visitInt(((Integer) this.value), p);
        } else if (this.value instanceof Long) {
            return v.visitLong(((Long) this.value), p);
        } else if (this.value instanceof Short) {
            return v.visitShort(((Short) this.value), p);
        } else if (this.value instanceof String) {
            return v.visitString(((String) this.value), p);
        } else if (this.value instanceof Class) {
            return v.visitType(this.typeMirrorFactory.createFromClass((Class<?>) this.value), p);
        } else if (this.value.getClass().isEnum()) {
            return v.visitEnumConstant(this.variableElementFactory.createEnum((Enum<?>) this.value), p);
        } else if (this.value instanceof Annotation) {
            return v.visitAnnotation(this.annotationFactory.fromAnnotation((Annotation) this.value), p);
        } else if (this.value instanceof Annotation[]) {
            return Arrays.stream(((Annotation[]) this.value))
                    .map(value1 -> new RuntimeAnnotationValue(this.typeMirrorFactory,
                                                              this.variableElementFactory,
                                                              this.annotationFactory,
                                                              value1))
                    .collect(Collectors.collectingAndThen(
                            ImmutableList.toImmutableList(),
                            values -> v.visitArray(values, p)
                    ));
        }
        return v.visitUnknown(this, p);
    }

    @Override
    public String toString() {
        return "RuntimeAnnotationValue{" +
                "typeMirrorFactory=" + this.typeMirrorFactory +
                ", variableElementFactory=" + this.variableElementFactory +
                ", annotationFactory=" + this.annotationFactory +
                ", value=" + this.value +
                "}";
    }

}
