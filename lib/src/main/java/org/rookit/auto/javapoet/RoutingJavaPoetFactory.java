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

import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeElementFactory;

import java.util.Collection;
import java.util.function.Predicate;

public final class RoutingJavaPoetFactory<T> implements JavaPoetFactory<T> {

    public static <E> JavaPoetFactory<E> create(final JavaPoetFactory<E> primaryFactory,
                                                final JavaPoetFactory<E> secondaryFactory,
                                                final Predicate<Property> primaryFilter,
                                                final ExtendedTypeElementFactory elementFactory) {
        return new RoutingJavaPoetFactory<>(primaryFactory, secondaryFactory, primaryFilter, elementFactory);
    }

    private final JavaPoetFactory<T> primaryFactory;
    private final JavaPoetFactory<T> secondaryFactory;
    private final Predicate<Property> primaryFilter;
    private final ExtendedTypeElementFactory elementFactory;

    private RoutingJavaPoetFactory(final JavaPoetFactory<T> primaryFactory,
                                   final JavaPoetFactory<T> secondaryFactory,
                                   final Predicate<Property> primaryFilter,
                                   final ExtendedTypeElementFactory elementFactory) {
        this.primaryFactory = primaryFactory;
        this.secondaryFactory = secondaryFactory;
        this.primaryFilter = primaryFilter;
        this.elementFactory = elementFactory;
    }

    @Override
    public StreamEx<T> create(final ExtendedTypeElement owner) {
        final Collection<Property> properties = owner.properties();
        final Collection<Property> primaryProperties = Lists.newArrayListWithCapacity(properties.size()/2);
        final Collection<Property> secondaryProperties = Lists.newArrayListWithCapacity(properties.size()/2);

        for (final Property property : properties) {
            if (this.primaryFilter.test(property)) {
                primaryProperties.add(property);
            }
            else {
                secondaryProperties.add(property);
            }
        }

        final ExtendedTypeElement primaryElement = this.elementFactory.changeProperties(owner, primaryProperties);
        final ExtendedTypeElement secondaryElement = this.elementFactory.changeProperties(owner, secondaryProperties);
        return this.primaryFactory.create(primaryElement)
                .append(this.secondaryFactory.create(secondaryElement));
    }

    @Override
    public StreamEx<T> create(final ExtendedTypeElement owner, final Property property) {
        return StreamEx.empty();
    }

    @Override
    public String toString() {
        return "RoutingJavaPoetFactory{" +
                "primaryFactory=" + this.primaryFactory +
                ", secondaryFactory=" + this.secondaryFactory +
                ", primaryFilter=" + this.primaryFilter +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
