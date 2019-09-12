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
package org.rookit.auto.javax.type.adapter;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.auto.javax.type.ExtendedTypeMirrorFactory;
import org.rookit.utils.adapt.Adapter;
import org.rookit.utils.guice.Collection;
import org.rookit.utils.guice.Optional;
import org.rookit.utils.optional.OptionalFactory;

@SuppressWarnings("MethodMayBeStatic")
public final class TypeAdapterModule extends AbstractModule {

    private static final Module MODULE = new TypeAdapterModule();

    public static Module getModule() {
        return MODULE;
    }

    private TypeAdapterModule() {}

    @Override
    protected void configure() {
        bind(TypeAdapters.class).to(TypeAdaptersImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Collection(unwrap = true)
    Adapter<ExtendedTypeMirror> collectionTypeUnwrapper() {
        return CollectionTypeMirrorAdapter.getSingleton();
    }

    @Provides
    @Singleton
    @Optional
    Adapter<ExtendedTypeMirror> optionalTypeMirrorUnwrapper(final TypeAdapters adapters,
                                                            final OptionalFactory optionalFactory,
                                                            final ExtendedTypeMirrorFactory mirrorFactory) {
        return adapters.createOptionalUnwrapper(optionalFactory, mirrorFactory);
    }
}
