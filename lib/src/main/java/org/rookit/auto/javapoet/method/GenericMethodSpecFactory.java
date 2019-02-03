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
package org.rookit.auto.javapoet.method;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeVariableName;
import org.rookit.auto.naming.NamingFactory;

import javax.lang.model.element.Modifier;


public final class GenericMethodSpecFactory implements MethodSpecFactory {

    private static final Modifier[] MODIFIERS = {Modifier.PUBLIC, Modifier.ABSTRACT};

    public static MethodSpecFactory create(final TypeVariableName typeVariableName,
                                           final NamingFactory namingFactory) {
        return new GenericMethodSpecFactory(typeVariableName, namingFactory);
    }

    private final TypeVariableName typeVariableName;
    private final NamingFactory namingFactory;

    private GenericMethodSpecFactory(final TypeVariableName typeVariableName,
                                     final NamingFactory namingFactory) {
        this.typeVariableName = typeVariableName;
        this.namingFactory = namingFactory;
    }

    @Override
    public MethodSpec create(final String propertyName,
                             final String prefix,
                             final String suffix,
                             final ParameterSpec... parameters) {
        return MethodSpec.methodBuilder(this.namingFactory.method(propertyName, prefix, suffix))
                .addParameters(ImmutableList.copyOf(parameters))
                .addModifiers(MODIFIERS)
                .returns(this.typeVariableName)
                .build();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("typeVariableName", this.typeVariableName)
                .add("namingFactory", this.namingFactory)
                .toString();
    }
}
