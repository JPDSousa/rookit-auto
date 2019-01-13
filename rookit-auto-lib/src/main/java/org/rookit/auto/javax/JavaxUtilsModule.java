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

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import org.rookit.auto.guice.Collection;
import org.rookit.auto.guice.Map;
import org.rookit.auto.guice.Optional;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

// TODO in order to solve over copulation, break this module per repetition type
public final class JavaxUtilsModule extends AbstractModule {

    private static final Module MODULE = new JavaxUtilsModule();

    public static Module getModule() {
        return MODULE;
    }

    private JavaxUtilsModule() {}

    @Override
    protected void configure() {
        bindOptional();
        bindCollection();
        bindMap();
    }

    private void bindMap() {
        final Multibinder<KeyedRepetitiveTypeMirror> map = newSetBinder(binder(),
                KeyedRepetitiveTypeMirror.class, Map.class);
        map.addBinding().toProvider(JavaMapProvider.class).in(Singleton.class);
        map.addBinding().toProvider(FastUtilInt2ObjectMapProvider.class).in(Singleton.class);
        // TODO missing other types of maps
    }

    private void bindCollection() {
        final Multibinder<RepetitiveTypeMirror> collection = newSetBinder(binder(),
                RepetitiveTypeMirror.class, Collection.class);
        collection.addBinding().toProvider(JavaCollectionProvider.class).in(Singleton.class);
        collection.addBinding().toProvider(JavaListProvider.class).in(Singleton.class);
        collection.addBinding().toProvider(JavaSetProvider.class).in(Singleton.class);
        collection.addBinding().toProvider(JavaQueueProvider.class).in(Singleton.class);
        collection.addBinding().toProvider(ApacheBagProvider.class).in(Singleton.class);
        // TODO missing other types of collections
    }

    private void bindOptional() {
        final Multibinder<RepetitiveTypeMirror> optional = newSetBinder(binder(),
                RepetitiveTypeMirror.class, Optional.class);
        optional.addBinding().toProvider(JavaOptionalProvider.class).in(Singleton.class);
        optional.addBinding().toProvider(GuavaOptionalProvider.class).in(Singleton.class);
        optional.addBinding().toProvider(RookitOptionalProvider.class).in(Singleton.class);
    }
}
