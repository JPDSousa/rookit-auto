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
package org.rookit.auto.entity.lazy;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;

public final class LazyPartialEntityFactory implements PartialEntityFactory {

    public static PartialEntityFactory create(final Provider<PartialEntityFactory> provider) {
        return new LazyPartialEntityFactory(provider);
    }

    private final Provider<PartialEntityFactory> partialEntityFactoryProvider;

    @Inject
    private LazyPartialEntityFactory(final Provider<PartialEntityFactory> partialEntityFactory) {
        this.partialEntityFactoryProvider = partialEntityFactory;
    }

    @Override
    public PartialEntity create(final ExtendedTypeElement element) {
        return this.partialEntityFactoryProvider.get().create(element);
    }
}
