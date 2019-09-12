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
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.rookit.auto.javax.runtime.RuntimeExecutableElementFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeMirrorFactory;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class RuntimeAnnotationMirrorFactoryImpl implements RuntimeAnnotationMirrorFactory {

    private final RuntimeTypeMirrorFactory mirrorFactory;
    private final RuntimeAnnotationValueFactory valueFactory;
    private final RuntimeExecutableElementFactory executableElementFactory;

    @Inject
    private RuntimeAnnotationMirrorFactoryImpl(
            final RuntimeTypeMirrorFactory mirrorFactory,
            final RuntimeAnnotationValueFactory valueFactory,
            final RuntimeExecutableElementFactory executableFactory) {
        this.mirrorFactory = mirrorFactory;
        this.valueFactory = valueFactory;
        this.executableElementFactory = executableFactory;
    }

    @Override
    public AnnotationMirror fromAnnotation(final Annotation annotation) {
        final DeclaredType declaredType = this.mirrorFactory.createDeclaredTypeFromClass(annotation.annotationType());
        final Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues
                = createElementValues(annotation);
        return new RuntimeAnnotationMirror(elementValues, annotation, declaredType);
    }

    @Override
    public List<AnnotationMirror> createFromAnnotatedElement(final AnnotatedElement annotatedElement) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .map(this::fromAnnotation)
                .collect(ImmutableList.toImmutableList());
    }

    private Map<? extends ExecutableElement, ? extends AnnotationValue> createElementValues(
            final Annotation annotation) {
        final ImmutableMap.Builder<ExecutableElement, AnnotationValue> builder = ImmutableMap.builder();

        for (final Method method : annotation.annotationType().getMethods()) {
            try {
                final Object value = method.invoke(annotation);
                builder.put(this.executableElementFactory.createFromExecutable(method),
                            this.valueFactory.createFromObject(value));
            } catch (final IllegalAccessException | InvocationTargetException e) {
                final String errMsg = String.format("Could not get value for field '%s' in annotation '%s'",
                                                    method.getName(), annotation.annotationType().getCanonicalName());
                throw new IllegalStateException(errMsg, e);
            }
        }

        return builder.build();
    }

    @Override
    public String toString() {
        return "RuntimeAnnotationMirrorFactoryImpl{" +
                "mirrorFactory=" + this.mirrorFactory +
                ", valueFactory=" + this.valueFactory +
                ", executableElementFactory=" + this.executableElementFactory +
                "}";
    }

}
