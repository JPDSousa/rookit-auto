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
package org.rookit.auto.javax.repetition.map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.rookit.auto.javax.repetition.KeyedRepetitiveTypeMirror;
import org.rookit.auto.javax.repetition.RepetitiveTypeMirrorFactory;
import org.rookit.utils.reflect.ExtendedClass;
import org.rookit.utils.reflect.ExtendedClassFactory;

final class FastUtilInt2ObjectMapProvider implements Provider<KeyedRepetitiveTypeMirror> {

    private final RepetitiveTypeMirrorFactory factory;
    private final ExtendedClassFactory classFactory;

    @Inject
    private FastUtilInt2ObjectMapProvider(final RepetitiveTypeMirrorFactory factory,
                                          final ExtendedClassFactory classFactory) {
        this.factory = factory;
        this.classFactory = classFactory;
    }

    @Override
    public KeyedRepetitiveTypeMirror get() {
        final ExtendedClass<?> extendedClass = this.classFactory.create(Int2ObjectMap.class);
        final ExtendedClass<?> keyClass = this.classFactory.create(int.class);
        return this.factory.createKeyed(extendedClass, keyClass, 0);
    }

    @Override
    public String toString() {
        return "FastUtilInt2ObjectMapProvider{" +
                "factory=" + this.factory +
                ", classFactory=" + this.classFactory +
                "}";
    }
}
