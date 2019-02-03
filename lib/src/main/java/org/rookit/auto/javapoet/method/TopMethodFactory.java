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
package org.rookit.auto.javapoet.method;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.MethodSpec;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.element.ExtendedTypeElement;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.stream.Stream;

import static java.lang.String.format;

public final class TopMethodFactory implements MethodFactory {

    public static MethodFactory create(final MethodFactory genericMethodFactory,
                                       final AnnotationBasedMethodFactory annotationFactory,
                                       final Collection<TypeBasedMethodFactory> typeFactories,
                                       final Messager messager) {
        return new TopMethodFactory(genericMethodFactory, annotationFactory, typeFactories, messager);
    }

    private final Collection<MethodFactory> typeFactories;
    private final AnnotationBasedMethodFactory annotationFactory;
    private final MethodFactory genericMethodFactory;
    private final Messager messager;

    private TopMethodFactory(final MethodFactory genericMethodFactory,
                             final AnnotationBasedMethodFactory annotationFactory,
                             final Collection<TypeBasedMethodFactory> typeFactories,
                             final Messager messager) {
        this.genericMethodFactory = genericMethodFactory;
        this.annotationFactory = annotationFactory;
        this.typeFactories = ImmutableList.copyOf(typeFactories);
        this.messager = messager;
    }

    @Override
    public Stream<MethodSpec> create(final ExtendedTypeElement owner, final ExtendedProperty property) {
        final String propertyName = property.name();
        if (this.annotationFactory.isCompatible(property)) {
            final String message = format("Using annotation factory to process %s", propertyName);
            this.messager.printMessage(Diagnostic.Kind.NOTE, message);
            return this.annotationFactory.create(owner, property);
        }

        for (final MethodFactory factory : this.typeFactories) {
            if (factory.isCompatible(property)) {
                final String message = format("Using type factory '%s' to process %s", factory, propertyName);
                this.messager.printMessage(Diagnostic.Kind.NOTE, message);
                return factory.create(owner, property);
            }
        }
        final String message = format("No compatible factory found for %s. Falling back to generic.", propertyName);
        this.messager.printMessage(Diagnostic.Kind.NOTE, message);
        return this.genericMethodFactory.create(owner, property);
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        if (this.genericMethodFactory.isCompatible(property)) {
            // already compatible with the fallback factory, so, compatible for sure!
            return true;
        }
        for (final MethodFactory typeFactory : this.typeFactories) {
            if (typeFactory.isCompatible(property)) {
                return true;
            }
        }
        return this.annotationFactory.isCompatible(property);
    }

    @Override
    public String toString() {
        return "TopMethodFactory{" +
                "typeFactories=" + this.typeFactories +
                ", annotationFactory=" + this.annotationFactory +
                ", genericMethodFactory=" + this.genericMethodFactory +
                ", messager=" + this.messager +
                "}";
    }
}
