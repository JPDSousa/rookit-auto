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
import com.google.inject.Provider;
import org.rookit.auto.javax.element.TypeParameterExtractor;
import org.rookit.utils.optional.OptionalFactory;
import org.rookit.utils.reflect.ClassVisitor;
import org.rookit.utils.reflect.ExtendedClass;

import javax.annotation.processing.Messager;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static java.lang.String.format;

public final class BaseExtendedTypeMirrorFactory implements ExtendedTypeMirrorFactory,
        ClassVisitor<ExtendedTypeMirror> {

    public static ExtendedTypeMirrorFactory create(final Types types,
                                                   final Messager messager,
                                                   final Elements elements,
                                                   final Provider<TypeParameterExtractor> extractor,
                                                   final OptionalFactory optionalFactory) {
        return new BaseExtendedTypeMirrorFactory(types, elements, messager, extractor, optionalFactory);
    }

    private final Types types;
    private final Elements elements;
    private final Messager messager;
    private final Provider<TypeParameterExtractor> extractor;
    private final OptionalFactory optionalFactory;

    @Inject
    private BaseExtendedTypeMirrorFactory(final Types types,
                                          final Elements elements,
                                          final Messager messager,
                                          final Provider<TypeParameterExtractor> extractor,
                                          final OptionalFactory optionalFactory) {
        this.types = types;
        this.elements = elements;
        this.messager = messager;
        this.extractor = extractor;
        this.optionalFactory = optionalFactory;
    }


    @Override
    public ExtendedTypeMirror createWithErasure(final Class<?> clazz) {
        return createWithErasure(this.elements.getTypeElement(clazz.getCanonicalName()).asType());
    }

    @Override
    public ExtendedTypeMirror createWithErasure(final TypeMirror typeMirror) {
        return create(this.types.erasure(typeMirror));
    }

    @Override
    public ExtendedTypeMirror create(final TypeMirror typeMirror) {
        if (typeMirror instanceof ExtendedTypeMirror) {
            final String errMsg = format("%s is already a %s. Bypassing creation.", typeMirror,
                    ExtendedTypeMirror.class.getName());
            this.messager.printMessage(Diagnostic.Kind.NOTE, errMsg);
            return (ExtendedTypeMirror) typeMirror;
        }
        return new ExtendedTypeMirrorImpl(typeMirror, this.types, this, this.extractor.get(), this.optionalFactory);
    }

    @Override
    public ExtendedTypeMirror create(final TypeKind typeKind) {
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
        return createWithErasure(clazz);
    }

    @Override
    public String toString() {
        return "BaseExtendedTypeMirrorFactory{" +
                "types=" + this.types +
                ", elements=" + this.elements +
                ", messager=" + this.messager +
                ", extractor=" + this.extractor +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
