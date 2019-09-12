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
package org.rookit.auto.javax.runtime.element;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.rookit.auto.javax.runtime.RuntimeElementFactory;
import org.rookit.auto.javax.runtime.RuntimeElementKindFactory;
import org.rookit.utils.guice.Cached;
import org.rookit.utils.guice.Uncached;

public final class ElementModule extends AbstractModule {

    private static final Module MODULE = new ElementModule();

    public static Module getModule() {
        return MODULE;
    }

    private ElementModule() {}

    @Override
    protected void configure() {
        bind(RuntimeElementFactory.class).to(Key.get(RuntimeElementFactory.class, Cached.class)).in(Singleton.class);
        bind(RuntimeElementFactory.class).annotatedWith(Uncached.class)
                .to(RuntimeElementFactoryImpl.class).in(Singleton.class);
        bind(RuntimeElementFactory.class).annotatedWith(Cached.class)
                .to(CachedRuntimeElementFactory.class).in(Singleton.class);
        bind(RuntimeElementKindFactory.class).to(RuntimeElementKindFactoryImpl.class).in(Singleton.class);
    }

}
