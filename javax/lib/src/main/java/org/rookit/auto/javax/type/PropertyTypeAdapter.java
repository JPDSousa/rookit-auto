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

import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.property.PropertyFactory;
import org.rookit.utils.adapt.Adapter;

import java.util.Collection;

public final class PropertyTypeAdapter implements Adapter<ExtendedTypeElement> {

    public static Adapter<ExtendedTypeElement> create(final PropertyFactory propertyFactory,
                                                      final Adapter<ExtendedTypeMirror> typeMirrorAdapter,
                                                      final Adapter<ExtendedTypeElement> upstreamAdapter) {
        return new PropertyTypeAdapter(propertyFactory, typeMirrorAdapter, upstreamAdapter);
    }

    private final PropertyFactory propertyFactory;
    private final Adapter<ExtendedTypeMirror> typeMirrorAdapter;
    private final Adapter<ExtendedTypeElement> upstreamAdapter;

    @Inject
    private PropertyTypeAdapter(final PropertyFactory propertyFactory,
                                final Adapter<ExtendedTypeMirror> typeMirrorAdapter,
                                final Adapter<ExtendedTypeElement> upstreamAdapter) {
        this.propertyFactory = propertyFactory;
        this.typeMirrorAdapter = typeMirrorAdapter;
        this.upstreamAdapter = upstreamAdapter;
    }

    @Override
    public ExtendedTypeElement adapt(final ExtendedTypeElement source) {
        final ExtendedTypeElement stage1 = this.upstreamAdapter.adapt(source);
        final Collection<Property> newProperties = StreamEx.of(stage1.properties())
                .map(this::adaptProperty)
                .toImmutableList();

        return new PropertyOverridingExtendedTypeElement(source, newProperties);
    }

    private Property adaptProperty(final Property property) {
        return this.propertyFactory.changeType(property, this.typeMirrorAdapter.adapt(property.type()));
    }

    @Override
    public String toString() {
        return "PropertyTypeAdapter{" +
                "propertyFactory=" + this.propertyFactory +
                ", typeMirrorAdapter=" + this.typeMirrorAdapter +
                ", upstreamAdapter=" + this.upstreamAdapter +
                "}";
    }
}
