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
package org.rookit.auto.javax.runtime.element;

import org.rookit.auto.javax.runtime.NameFactory;
import org.rookit.auto.javax.runtime.RuntimeModifierFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeMirrorFactory;
import org.rookit.auto.javax.runtime.entity.RuntimeEntityVisitor;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Set;

final class RuntimeElementVisitor<P> implements RuntimeEntityVisitor<Element, P> {


    @Override
    public Element visitClass(final Class<?> clazz, final P parameter) {
    }

    @Override
    public Element visitMethod(final Method method, final P parameter) {
        return null;
    }

    @Override
    public Element visitConstructor(final Constructor<?> constructor, final P parameter) {
        return null;
    }

    @Override
    public Element visitPackage(final Package pack, final P parameter) {
        return null;
    }

    @Override
    public Element visitTypeVariable(final TypeVariable<?> typeVariable, final P parameter) {
        return null;
    }

    @Override
    public Element visitParameter(final Parameter reflectParameter, final P parameter) {
        return null;
    }

}
