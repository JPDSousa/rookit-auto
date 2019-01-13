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
import org.rookit.auto.javax.element.TypeParameterExtractor;
import org.rookit.utils.type.ExtendedClass;

import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

public final class BaseRepetitiveTypeMirrorFactory implements RepetitiveTypeMirrorFactory {

    public static RepetitiveTypeMirrorFactory create(final TypeParameterExtractor extractor,
                                                     final ExtendedTypeMirrorFactory delegateFactory) {
        return new BaseRepetitiveTypeMirrorFactory(extractor, delegateFactory);
    }

    private final TypeParameterExtractor extractor;
    private final ExtendedTypeMirrorFactory delegateFactory;

    @Inject
    private BaseRepetitiveTypeMirrorFactory(final TypeParameterExtractor extractor,
                                            final ExtendedTypeMirrorFactory delegateFactory) {
        this.extractor = extractor;
        this.delegateFactory = delegateFactory;
    }

    private Function<TypeMirror, TypeMirror> extractParam(final int parameterIndex) {
        return typeMirror -> this.extractor.extract(typeMirror).get(parameterIndex);
    }

    @Override
    public RepetitiveTypeMirror create(final TypeMirror delegate, final int parameterIndex) {
        return new RepetitiveTypeMirrorImpl(this.delegateFactory.create(delegate), extractParam(parameterIndex));
    }

    @Override
    public RepetitiveTypeMirror create(final ExtendedClass<?> delegate, final int parameterIndex) {
        return create(this.delegateFactory.create(delegate), parameterIndex);
    }

    @Override
    public KeyedRepetitiveTypeMirror createKeyed(final TypeMirror delegate, final int keyIndex, final int valueIndex) {
        return new KeyedRepetitiveTypeMirrorImpl(create(delegate, valueIndex), extractParam(keyIndex));
    }

    @Override
    public KeyedRepetitiveTypeMirror createKeyed(final ExtendedClass<?> delegate,
                                                 final int keyIndex,
                                                 final int valueIndex) {
        return new KeyedRepetitiveTypeMirrorImpl(create(delegate, valueIndex), extractParam(keyIndex));
    }

    @Override
    public KeyedRepetitiveTypeMirror createKeyed(final TypeMirror delegate,
                                                 final ExtendedClass<?> constantKey,
                                                 final int valueIndex) {
        final ExtendedTypeMirror typeKey = this.delegateFactory.create(constantKey);
        return new KeyedRepetitiveTypeMirrorImpl(create(delegate, valueIndex), typeMirror -> typeKey);
    }

    @Override
    public KeyedRepetitiveTypeMirror createKeyed(final ExtendedClass<?> delegate,
                                                 final ExtendedClass<?> constantKey,
                                                 final int valueIndex) {
        final ExtendedTypeMirror typeKey = this.delegateFactory.create(constantKey);
        return new KeyedRepetitiveTypeMirrorImpl(create(delegate, valueIndex), typeMirror -> typeKey);
    }

    @Override
    public String toString() {
        return "BaseRepetitiveTypeMirrorFactory{" +
                "extractor=" + this.extractor +
                ", delegateFactory=" + this.delegateFactory +
                "}";
    }
}
