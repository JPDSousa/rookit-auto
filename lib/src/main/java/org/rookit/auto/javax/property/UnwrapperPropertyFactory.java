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
package org.rookit.auto.javax.property;

import com.google.inject.Inject;
import org.rookit.auto.javax.ExtendedTypeMirror;
import org.rookit.auto.javax.JavaxRepetition;
import org.rookit.auto.javax.JavaxRepetitionFactory;

public final class UnwrapperPropertyFactory implements ExtendedPropertyFactory {

    public static ExtendedPropertyFactory create(final ExtendedPropertyFactory delegate,
                                                 final JavaxRepetitionFactory repetitionFactory) {
        return new UnwrapperPropertyFactory(delegate, repetitionFactory);
    }

    private final ExtendedPropertyFactory delegate;
    private final JavaxRepetitionFactory repetitionFactory;

    @Inject
    private UnwrapperPropertyFactory(final ExtendedPropertyFactory delegate,
                                     final JavaxRepetitionFactory repetitionFactory) {
        this.delegate = delegate;
        this.repetitionFactory = repetitionFactory;
    }

    @Override
    public ExtendedProperty create(final String name, final ExtendedTypeMirror type) {
        return create(name, type, this.repetitionFactory.fromTypeMirror(type));
    }

    @Override
    public ExtendedProperty create(final String name, final ExtendedTypeMirror type, final JavaxRepetition repetition) {
        return this.delegate.create(name, repetition.unwrap(type), repetition);
    }

    @Override
    public ExtendedProperty createFinal(final String name, final ExtendedTypeMirror type) {
        return createFinal(name, type, this.repetitionFactory.fromTypeMirror(type));
    }

    @Override
    public ExtendedProperty createFinal(final String name,
                                        final ExtendedTypeMirror type,
                                        final JavaxRepetition repetition) {
        return this.delegate.createFinal(name, repetition.unwrap(type), repetition);
    }

    @Override
    public String toString() {
        return "UnwrapperPropertyFactory{" +
                "delegate=" + this.delegate +
                ", repetitionFactory=" + this.repetitionFactory +
                "}";
    }
}
