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
package org.rookit.auto.javax.element;

import com.google.common.collect.ImmutableSet;
import org.rookit.utils.optional.Optional;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;

public interface ElementUtils {

    default <T extends TypeMirror> Collection<T> intersection(final Collection<T> types,
                                                              final Collection<T> moreTypes) {
        final ImmutableSet.Builder<T> builder = ImmutableSet.builder();
        for (final T type : types) {
            for (final T anotherType : moreTypes) {
                if (isSameType(type, anotherType)) {
                    builder.add(type);
                }
            }
        }
        return builder.build();
    }

    boolean isSameType(TypeMirror type, TypeMirror anotherType);

    TypeMirror erasure(Class<?> clazz);

    boolean isSameTypeErasure(TypeMirror type, TypeMirror anotherType);

    Collection<? extends TypeMirror> typeParameters(TypeMirror type);

    TypeMirror primitive(TypeKind typeKind);

    TypeMirror boxIfPrimitive(TypeMirror typeMirror);

    Optional<Element> toElement(TypeMirror typeMirror);

    boolean isConventionElement(AnnotatedConstruct element);

}
