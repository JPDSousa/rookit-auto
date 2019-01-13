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

import javax.annotation.processing.Messager;
import javax.lang.model.type.TypeMirror;

final class KeyedRepetitionImpl extends AbstractRepetition<KeyedRepetitiveTypeMirror> implements JavaxKeyedRepetition {

    KeyedRepetitionImpl(final String name,
                        final Messager messager,
                        final boolean multi,
                        final boolean optional,
                        final KeyedRepetitiveTypeMirror repetitiveTypeMirror) {
        super(messager, optional, repetitiveTypeMirror, multi, name);
    }

    @Override
    public boolean isKeyed() {
        // all instances of this class represent keyed repetitions
        return true;
    }

    @Override
    public TypeMirror unwrapKey(final TypeMirror typeMirror) {
        return repetitiveTypeMirror().unwrapKey(typeMirror);
    }
}
