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

import com.google.inject.Inject;
import org.rookit.auto.javax.runtime.RuntimeElementKindFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;

final class RuntimeEntityFactoryImpl implements RuntimeEntityFactory {

    private final RuntimeElementKindFactory elementKindFactory;

    @Inject
    private RuntimeEntityFactoryImpl(final RuntimeElementKindFactory elementKindFactory) {
        this.elementKindFactory = elementKindFactory;
    }

    @Override
    public RuntimeEntity fromClass(final Class<?> clazz) {
        return new ClassEntity(clazz, this.elementKindFactory.createFromClass(clazz));
    }

    @Override
    public RuntimeEntity fromMethod(final Method method) {
        return new MethodEntity(method);
    }

    @Override
    public RuntimeEntity fromConstructor(final Constructor<?> constructor) {
        return new ConstructorEntity(constructor);
    }

    @Override
    public RuntimeEntity fromParameter(final Parameter parameter) {
        return new ParameterEntity(parameter);
    }

    @Override
    public RuntimeEntity fromPackage(final Package pack) {
        return new PackageEntity(pack);
    }

    @Override
    public RuntimeEntity fromTypeVariable(final TypeVariable<?> typeVariable) {
        return new TypeVariableEntity(typeVariable);
    }

    @Override
    public String toString() {
        return "RuntimeEntityFactoryImpl{" +
                "elementKindFactory=" + this.elementKindFactory +
                "}";
    }

}
