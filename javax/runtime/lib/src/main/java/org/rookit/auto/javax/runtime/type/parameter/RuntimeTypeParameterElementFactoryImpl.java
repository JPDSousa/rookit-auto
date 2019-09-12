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
import com.google.inject.Inject;
import org.rookit.auto.javax.runtime.NameFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeMirrorFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeParameterElementFactory;
import org.rookit.auto.javax.runtime.annotation.RuntimeAnnotatedConstructFactory;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;

final class RuntimeTypeParameterElementFactoryImpl implements RuntimeTypeParameterElementFactory {

    private final RuntimeAnnotatedConstructFactory annotatedFactory;
    private final RuntimeTypeMirrorFactory mirrorFactory;
    private final NameFactory nameFactory;

    @Inject
    private RuntimeTypeParameterElementFactoryImpl(
            final RuntimeAnnotatedConstructFactory annotatedFactory,
            final RuntimeTypeMirrorFactory mirrorFactory,
            final NameFactory nameFactory) {
        this.annotatedFactory = annotatedFactory;
        this.mirrorFactory = mirrorFactory;
        this.nameFactory = nameFactory;
    }

    @Override
    public Collection<TypeParameterElement> createFrom(final Element parameterizedElement,
                                                       final GenericDeclaration genericDeclaration) {
        return Arrays.stream(genericDeclaration.getTypeParameters())
                .map(typeVariable -> createFrom(parameterizedElement, typeVariable))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public TypeParameterElement createFrom(
            final Element parameterizedElement,
            final TypeVariable<?> typeVariable) {
        final javax.lang.model.type.TypeVariable typeMirror = this.mirrorFactory.createFromTypeVariable(typeVariable);
        final TypeMirror upperBound = typeMirror.getUpperBound();
        final Collection<? extends TypeMirror> bounds = (upperBound instanceof IntersectionType)
                ? ((IntersectionType) upperBound).getBounds()
                : ImmutableList.of(upperBound);

        return new RuntimeTypeParameterElement(
                typeMirror,
                this.annotatedFactory.createFromAnnotatedElement(typeVariable),
                parameterizedElement,
                bounds,
                this.nameFactory.create(typeVariable.getName())
        );
    }

    @Override
    public String toString() {
        return "RuntimeTypeParameterElementFactoryImpl{" +
                "annotatedFactory=" + this.annotatedFactory +
                ", mirrorFactory=" + this.mirrorFactory +
                ", nameFactory=" + this.nameFactory +
                "}";
    }

}
