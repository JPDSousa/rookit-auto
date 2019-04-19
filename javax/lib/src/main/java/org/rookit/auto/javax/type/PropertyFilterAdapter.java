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
import org.rookit.utils.adapt.Adapter;

import java.util.Collection;
import java.util.function.Predicate;

final class PropertyFilterAdapter implements Adapter<ExtendedTypeElement> {

    public static Adapter<ExtendedTypeElement> create(final Predicate<Property> propertyFilter) {
        return new PropertyFilterAdapter(propertyFilter);
    }

    private final Predicate<Property> propertyFilter;

    @Inject
    private PropertyFilterAdapter(final Predicate<Property> propertyFilter) {
        this.propertyFilter = propertyFilter;
    }

    @Override
    public ExtendedTypeElement adapt(final ExtendedTypeElement source) {
        final Collection<Property> newProperties = StreamEx.of(source.properties())
                .filter(this.propertyFilter)
                .toImmutableList();

        return new PropertyOverridingExtendedTypeElement(source, newProperties);
    }

    @Override
    public String toString() {
        return "PropertyFilterAdapter{" +
                "propertyFilter=" + this.propertyFilter +
                "}";
    }
}
