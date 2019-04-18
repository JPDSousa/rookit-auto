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
package org.rookit.auto.javapoet;

import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.type.ExtendedTypeElement;

import java.util.Collection;

// This type is useful to facade a multiple method factories into a single one.
public final class MultiJavaPoetFactory<T> implements JavaPoetFactory<T> {

    public static <E> JavaPoetFactory<E> create(final Collection<JavaPoetFactory<E>> factories) {
        return new MultiJavaPoetFactory<>(factories);
    }

    private final Collection<JavaPoetFactory<T>> factories;

    @Inject
    private MultiJavaPoetFactory(final Collection<JavaPoetFactory<T>> factories) {
        this.factories = factories;
    }

    @Override
    public StreamEx<T> create(final ExtendedTypeElement owner) {
        return StreamEx.of(this.factories)
                .flatMap(factory -> factory.create(owner));
    }

    @Override
    public StreamEx<T> create(final ExtendedTypeElement owner, final Property property) {
        return StreamEx.empty();
    }

    @Override
    public String toString() {
        return "MultiJavaPoetFactory{" +
                "factories=" + this.factories +
                "}";
    }
}
