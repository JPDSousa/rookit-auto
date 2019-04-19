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
package org.rookit.auto.javax.type;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import org.rookit.auto.guice.Flat;
import org.rookit.auto.javax.property.PropertyFactory;
import org.rookit.utils.adapt.Adapter;
import org.rookit.utils.guice.Immutable;
import org.rookit.utils.guice.Mutable;
import org.rookit.utils.guice.Optional;
import org.rookit.utils.optional.OptionalFactory;

import java.util.function.Predicate;

@SuppressWarnings("MethodMayBeStatic")
public final class ExtendedTypeModule extends AbstractModule {

    private static final Module MODULE = new ExtendedTypeModule();

    public static Module getModule() {
        return MODULE;
    }

    private ExtendedTypeModule() {}

    @SuppressWarnings({"AnonymousInnerClassMayBeStatic", "AnonymousInnerClass", "EmptyClass"})
    @Override
    protected void configure() {
        bind(ExtendedTypeElementFactory.class).to(BaseExtendedTypeElementFactory.class).in(Singleton.class);
        bind(ElementUtils.class).to(ElementUtilsImpl.class).in(Singleton.class);
        bind(TypeParameterExtractor.class).to(TypeVisitorParameterExtractor.class).in(Singleton.class);
        bind(ExtendedTypeMirrorFactory.class).to(BaseExtendedTypeMirrorFactory.class).in(Singleton.class);
        bind(new TypeLiteral<Predicate<ExtendedTypeMirror>>() {}).annotatedWith(Optional.class)
                .to(OptionalFilter.class).in(Singleton.class);
        bind(new TypeLiteral<Adapter<ExtendedTypeElement>>() {}).annotatedWith(Flat.class)
                .to(PropertyFlatAdapter.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Mutable
    Adapter<ExtendedTypeElement> mutablePropertyFilteredElement(
            @Mutable final Predicate<org.rookit.auto.javax.property.Property> filter) {
        return PropertyFilterAdapter.create(filter);
    }

    @Provides
    @Singleton
    @Immutable
    Adapter<ExtendedTypeElement> immutablePropertyFilteredElement(
            @Immutable final Predicate<org.rookit.auto.javax.property.Property> filter) {
        return PropertyFilterAdapter.create(filter);
    }

    @Provides
    @Singleton
    @Optional
    Adapter<ExtendedTypeElement> optionalUnwrapper(
            final PropertyFactory propertyFactory,
            @Optional final Adapter<ExtendedTypeMirror> mirrorAdapter,
            @Optional final Adapter<ExtendedTypeElement> propertyAdapter) {
        return PropertyTypeAdapter.create(propertyFactory, mirrorAdapter, propertyAdapter);
    }

    @Provides
    @Singleton
    @Optional
    Adapter<ExtendedTypeMirror> optionalTypeMirrorUnwrapper(final OptionalFactory optionalFactory,
                                                            final ExtendedTypeMirrorFactory mirrorFactory) {
        return new OptionalExtendedTypeMirrorAdapter(optionalFactory, mirrorFactory);
    }

}
