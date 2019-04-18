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
package org.rookit.auto.javapoet.type;

import com.google.common.base.MoreObjects;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javapoet.JavaPoetFactory;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.property.ExtendedPropertyExtractor;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.SingleTypeSourceFactory;

import java.util.Collection;
import java.util.stream.Collectors;

public final class PropertyBasedTypeSourceFactory extends AbstractInterfaceTypeSourceFactory {

    public static SingleTypeSourceFactory create(final JavaPoetParameterResolver parameterResolver,
                                                 final JavaPoetFactory<MethodSpec> javaPoetFactory,
                                                 final TypeSourceFactory adapter,
                                                 final ExtendedPropertyExtractor extractor) {
        return new PropertyBasedTypeSourceFactory(parameterResolver, adapter, extractor, javaPoetFactory);
    }

    private final ExtendedPropertyExtractor extractor;
    private final JavaPoetFactory<MethodSpec> javaPoetFactory;

    private PropertyBasedTypeSourceFactory(final JavaPoetParameterResolver parameterResolver,
                                           final TypeSourceFactory adapter,
                                           final ExtendedPropertyExtractor extractor,
                                           final JavaPoetFactory<MethodSpec> javaPoetFactory) {
        super(parameterResolver, adapter);
        this.extractor = extractor;
        this.javaPoetFactory = javaPoetFactory;
    }

    @Override
    protected Collection<MethodSpec> methodsFor(final ExtendedTypeElement element) {
        return this.extractor.fromType(element)
                .flatMap(property -> this.javaPoetFactory.create(element, property))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("extractor", this.extractor)
                .add("methodFactory", this.javaPoetFactory)
                .toString();
    }
}
