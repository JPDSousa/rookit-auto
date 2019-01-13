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
package org.rookit.auto.javax;

import com.google.inject.Inject;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.utils.type.ClassVisitor;
import org.rookit.utils.type.ExtendedClass;

import javax.annotation.processing.Messager;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static java.lang.String.format;

public final class BaseExtendedTypeMirrorFactory implements ExtendedTypeMirrorFactory,
        ClassVisitor<ExtendedTypeMirror> {

    public static ExtendedTypeMirrorFactory create(final ElementUtils utils, final Types types, final Messager messager) {
        return new BaseExtendedTypeMirrorFactory(utils, types, messager);
    }

    private final ElementUtils utils;
    private final Types types;
    private final Messager messager;

    @Inject
    private BaseExtendedTypeMirrorFactory(final ElementUtils utils,
                                          final Types types,
                                          final Messager messager) {
        this.utils = utils;
        this.types = types;
        this.messager = messager;
    }

    @Override
    public ExtendedTypeMirror create(final TypeMirror typeMirror) {
        if (typeMirror instanceof ExtendedTypeMirror) {
            final String errMsg = format("%s is already a %s. Bypassing creation.", typeMirror,
                    ExtendedTypeMirror.class.getName());
            this.messager.printMessage(Diagnostic.Kind.NOTE, errMsg);
            return (ExtendedTypeMirror) typeMirror;
        }
        return new ExtendedTypeMirrorImpl(typeMirror, this.utils);
    }

    private ExtendedTypeMirror create(final TypeKind typeKind) {
        return create(this.types.getPrimitiveType(typeKind));
    }

    @Override
    public ExtendedTypeMirror create(final ExtendedClass<?> clazz) {
        return clazz.accept(this);
    }

    @Override
    public ExtendedTypeMirror booleanClass() {
        return create(TypeKind.BOOLEAN);
    }

    @Override
    public ExtendedTypeMirror byteClass() {
        return create(TypeKind.BYTE);
    }

    @Override
    public ExtendedTypeMirror shortClass() {
        return create(TypeKind.SHORT);
    }

    @Override
    public ExtendedTypeMirror intClass() {
        return create(TypeKind.INT);
    }

    @Override
    public ExtendedTypeMirror floatClass() {
        return create(TypeKind.FLOAT);
    }

    @Override
    public ExtendedTypeMirror doubleClass() {
        return create(TypeKind.DOUBLE);
    }

    @Override
    public ExtendedTypeMirror longClass() {
        return create(TypeKind.LONG);
    }

    @Override
    public ExtendedTypeMirror regularClass(final Class<?> clazz) {
        return create(this.utils.erasure(clazz));
    }

    @Override
    public String toString() {
        return "BaseExtendedTypeMirrorFactory{" +
                "utils=" + this.utils +
                ", types=" + this.types +
                ", messager=" + this.messager +
                "}";
    }
}
