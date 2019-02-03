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
package org.rookit.auto.javapoet.method.annotation;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;
import com.squareup.javapoet.MethodSpec;
import one.util.streamex.StreamEx;
import org.apache.commons.text.WordUtils;
import org.rookit.auto.javapoet.method.AnnotationBasedMethodFactory;
import org.rookit.auto.javapoet.method.MethodFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyAdapter;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.convention.annotation.Property;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Stream;

public final class PropertyMethodFactory implements AnnotationBasedMethodFactory {

    public static AnnotationBasedMethodFactory create(final PropertyExtractor propertyExtractor,
                                                      final Provider<MethodFactory> methodFactory,
                                                      final PropertyAdapter adapter) {
        return new PropertyMethodFactory(propertyExtractor, methodFactory, adapter);
    }

    private final PropertyExtractor propertyExtractor;
    private final Provider<MethodFactory> methodFactory;
    private final PropertyAdapter adapter;

    private PropertyMethodFactory(final PropertyExtractor propertyExtractor,
                                  final Provider<MethodFactory> methodFactory,
                                  final PropertyAdapter adapter) {
        this.propertyExtractor = propertyExtractor;
        this.methodFactory = methodFactory;
        this.adapter = adapter;
    }

    @Override
    public Collection<Class<? extends Annotation>> supportedAnnotations() {
        return ImmutableList.of(Property.class);
    }

    @Override
    public Stream<MethodSpec> create(final ExtendedTypeElement owner, final ExtendedProperty property) {
        return property.typeAsElement()
                .map(returnType -> createMethods(property, returnType))
                .orElse(StreamEx.empty());
    }

    private Stream<MethodSpec> createMethods(final ExtendedProperty property, final ExtendedTypeElement returnType) {
        final MethodFactory methodFactory = this.methodFactory.get();

        return this.propertyExtractor.fromType(returnType)
                .map(nestedProperty -> concat(property, nestedProperty))
                .filter(methodFactory::isCompatible)
                .flatMap(nestedProperty -> methodFactory.create(returnType, nestedProperty));
    }

    private ExtendedProperty concat(final ExtendedProperty property, final ExtendedProperty nestedProperty) {
        final String name = property.name() + WordUtils.capitalize(nestedProperty.name());
        return this.adapter.changeName(nestedProperty, name);
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        return property.isContainer();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("propertyExtractor", this.propertyExtractor)
                .add("methodFactory", this.methodFactory)
                .add("adapter", this.adapter)
                .toString();
    }
}
