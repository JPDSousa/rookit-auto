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

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.JavaPoetFactory;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.property.PropertyFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.convention.annotation.Entity;
import org.rookit.utils.optional.Optional;

import javax.lang.model.element.Element;
import java.util.Objects;

public final class EntityMethodFactory implements JavaPoetFactory<MethodSpec> {

    public static JavaPoetFactory<MethodSpec> create(final MethodSpecFactory methodSpecFactory,
                                                     final PropertyFactory propertyFactory) {
        return new EntityMethodFactory(methodSpecFactory, propertyFactory);
    }

    private final MethodSpecFactory methodSpecFactory;
    private final PropertyFactory propertyFactory;

    @Inject
    private EntityMethodFactory(final MethodSpecFactory methodSpecFactory,
                                final PropertyFactory propertyFactory) {
        this.methodSpecFactory = methodSpecFactory;
        this.propertyFactory = propertyFactory;
    }

    @Override
    public StreamEx<MethodSpec> create(final ExtendedTypeElement owner) {
        return StreamEx.of(owner.properties())
                .map(this.propertyFactory::extend)
                .map(ExtendedProperty::typeAsElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(returnType -> Objects.nonNull(returnType.getAnnotation(Entity.class)))
                .map(this::createEntityMethod);
    }

    @Override
    public StreamEx<MethodSpec> create(final ExtendedTypeElement owner, final Property property) {
        return StreamEx.empty();
    }

    private MethodSpec createEntityMethod(final Element element) {
        final String elementName = element.getSimpleName().toString();
        final TypeName typeName = TypeName.get(element.asType());
        final ParameterSpec parameter = ParameterSpec.builder(typeName, elementName)
                .build();
        return this.methodSpecFactory.create(elementName, parameter);
    }

    @Override
    public String toString() {
        return "EntityMethodFactory{" +
                "methodSpecFactory=" + this.methodSpecFactory +
                ", propertyFactory=" + this.propertyFactory +
                "}";
    }
}
