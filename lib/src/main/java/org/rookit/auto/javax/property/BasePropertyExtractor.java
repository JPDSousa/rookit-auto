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
package org.rookit.auto.javax.property;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.ExtendedExecutableElement;
import org.rookit.auto.javax.ExtendedExecutableElementFactory;
import org.rookit.convention.annotation.Property;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BasePropertyExtractor implements PropertyExtractor {

    public static PropertyExtractor create(final ExtendedPropertyFactory propertyFactory,
                                           final ExtendedExecutableElementFactory executableFactory) {
        return new BasePropertyExtractor(executableFactory, propertyFactory);
    }

    private final ExtendedExecutableElementFactory executableElementFactory;
    private final ExtendedPropertyFactory propertyFactory;
    private final Multimap<String, ExtendedProperty> propertyCache;

    @Inject
    private BasePropertyExtractor(final ExtendedExecutableElementFactory executableFactory,
                                  final ExtendedPropertyFactory propertyFactory) {
        this.executableElementFactory = executableFactory;
        this.propertyFactory = propertyFactory;
        this.propertyCache = HashMultimap.create();
    }

    @Override
    public Stream<ExtendedProperty> fromType(final TypeElement element) {
        final String key = element.getQualifiedName().toString();
        if (this.propertyCache.containsKey(key)) {
            return this.propertyCache.get(key).stream();
        }

        final Set<ExtendedProperty> properties = StreamEx.of(element.getEnclosedElements().stream())
                .filter(el -> el.getKind() == ElementKind.METHOD)
                .filter(el -> Objects.nonNull(el.getAnnotation(Property.class)))
                .select(ExecutableElement.class)
                .map(this.executableElementFactory::create)
                .map(this::createProperty)
                .collect(Collectors.toSet());
        this.propertyCache.putAll(key, properties);
        return properties.stream();
    }

    private ExtendedProperty createProperty(final ExtendedExecutableElement executableElement) {
        final Property annotation = executableElement.getAnnotation(Property.class);
        return annotation.isSettable()
                ? this.propertyFactory.createFinal(executableElement)
                : this.propertyFactory.create(executableElement);
    }

    @Override
    public String toString() {
        return "BasePropertyExtractor{" +
                "executableElementFactory=" + this.executableElementFactory +
                ", propertyFactory=" + this.propertyFactory +
                ", propertyCache=" + this.propertyCache +
                "}";
    }
}
