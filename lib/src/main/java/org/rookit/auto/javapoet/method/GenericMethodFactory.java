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

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.element.ExtendedTypeElement;

import java.util.stream.Stream;

public final class GenericMethodFactory implements MethodFactory {

    public static MethodFactory create(final MethodSpecFactory methodSpecFactory, final String methodPrefix) {
        return new GenericMethodFactory(methodSpecFactory, methodPrefix, false);
    }

    public static MethodFactory createWithoutFinals(final MethodSpecFactory methodSpecFactory,
                                                    final String methodPrefix) {
        return new GenericMethodFactory(methodSpecFactory, methodPrefix, true);
    }

    private final MethodSpecFactory methodSpecFactory;
    private final String methodPrefix;
    private final boolean filterFinals;

    private GenericMethodFactory(final MethodSpecFactory methodSpecFactory,
                                 final String methodPrefix,
                                 final boolean filterFinals) {
        this.methodSpecFactory = methodSpecFactory;
        this.methodPrefix = methodPrefix;
        this.filterFinals = filterFinals;
    }

    @Override
    public Stream<MethodSpec> create(ExtendedTypeElement owner, final ExtendedProperty property) {
        return Stream.of(getGenericMethod(property));
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        return !(this.filterFinals && property.isFinal());
    }

    private MethodSpec getGenericMethod(final ExtendedProperty property) {
        final String propertyName = property.name();
        final ParameterSpec param = ParameterSpec.builder(TypeName.get(property.type()), propertyName).build();

        return this.methodSpecFactory.create(propertyName, this.methodPrefix, param);
    }

    @Override
    public String toString() {
        return "GenericMethodFactory{" +
                "methodSpecFactory=" + this.methodSpecFactory +
                ", methodPrefix='" + this.methodPrefix + '\'' +
                ", filterFinals=" + this.filterFinals +
                "}";
    }
}
