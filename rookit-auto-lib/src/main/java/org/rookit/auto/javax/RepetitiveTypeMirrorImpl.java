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

import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.function.Function;

final class RepetitiveTypeMirrorImpl implements RepetitiveTypeMirror {

    private final ExtendedTypeMirror delegate;
    private final Function<TypeMirror, ExtendedTypeMirror> unwrapper;

    RepetitiveTypeMirrorImpl(final ExtendedTypeMirror delegate,
                             final Function<TypeMirror, ExtendedTypeMirror> unwrapper) {
        this.delegate = delegate;
        this.unwrapper = unwrapper;
    }

    @Override
    public ExtendedTypeMirror unwrap(final ExtendedTypeMirror type) {
        return this.unwrapper.apply(type);
    }

    @Override
    public boolean isSameTypeErasure(final ExtendedTypeMirror other) {
        return this.delegate.isSameTypeErasure(other);
    }

    @Override
    public boolean isSameType(final ExtendedTypeMirror other) {
        return this.delegate.isSameType(other);
    }

    @Override
    public ExtendedTypeMirror erasure() {
        return this.delegate.erasure();
    }

    @Override
    public Collection<? extends ExtendedTypeMirror> typeParameters() {
        return this.delegate.typeParameters();
    }

    @Override
    public TypeMirror original() {
        return this.delegate.original();
    }

    @Override
    public ExtendedTypeMirror boxIfPrimitive() {
        return this.delegate.boxIfPrimitive();
    }

    @Override
    public String toString() {
        return "RepetitiveTypeMirrorImpl{" +
                "delegate=" + this.delegate +
                ", unwrapper=" + this.unwrapper +
                "}";
    }
}
