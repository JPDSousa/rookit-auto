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
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import org.rookit.auto.javapoet.method.AnnotationBasedMethodFactory;
import org.rookit.auto.javapoet.method.MethodSpecFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.convention.annotation.Entity;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public final class EntityMethodFactory implements AnnotationBasedMethodFactory {

    public static AnnotationBasedMethodFactory create(final Types types, final MethodSpecFactory methodSpecFactory) {
        return new EntityMethodFactory(types, methodSpecFactory);
    }

    private final Types types;
    private final MethodSpecFactory methodSpecFactory;

    private EntityMethodFactory(final Types types, final MethodSpecFactory methodSpecFactory) {
        this.types = types;
        this.methodSpecFactory = methodSpecFactory;
    }

    @Override
    public Collection<Class<? extends Annotation>> supportedAnnotations() {
        return ImmutableList.of(Entity.class);
    }

    @Override
    public Stream<MethodSpec> create(ExtendedTypeElement owner, final ExtendedProperty property) {
        final Element returnType = getReturnType(property);
        return Stream.of(createEntityMethod(returnType));
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        final Element returnType = getReturnType(property);
        return Objects.nonNull(returnType) && Objects.nonNull(returnType.getAnnotation(Entity.class));
    }

    private MethodSpec createEntityMethod(final Element element) {
        final String elementName = element.getSimpleName().toString();
        final TypeName typeName = TypeName.get(element.asType());
        final ParameterSpec parameter = ParameterSpec.builder(typeName, elementName)
                .build();
        return this.methodSpecFactory.create(elementName, parameter);
    }

    private Element getReturnType(final ExtendedProperty executable) {
        return this.types.asElement(executable.type());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("types", this.types)
                .add("methodSpecFactory", this.methodSpecFactory)
                .toString();
    }
}
