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
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.javapoet.naming.JavaPoetParameterResolver;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractInterfaceTypeSourceFactory implements SingleTypeSourceFactory {

    private final JavaPoetParameterResolver parameterResolver;
    private final TypeSourceAdapter adapter;

    protected AbstractInterfaceTypeSourceFactory(final JavaPoetParameterResolver parameterResolver,
                                                 final TypeSourceAdapter adapter) {
        this.parameterResolver = parameterResolver;
        this.adapter = adapter;
    }

    @Override
    public TypeSource create(final Identifier identifier,
                             final ExtendedTypeElement element) {
        final ClassName className = ClassName.get(identifier.packageName().fullName(), identifier.name());

        final TypeSpec spec = TypeSpec.interfaceBuilder(className)
                .addTypeVariables(this.parameterResolver.createParameters(element))
                .addMethods(methodsFor(element))
                .addSuperinterfaces(parentNamesOf(element))
                .addModifiers(Modifier.PUBLIC)
                .build();
        return this.adapter.fromTypeSpec(identifier, spec);
    }

    protected abstract Collection<MethodSpec> methodsFor(ExtendedTypeElement element);

    protected StreamEx<TypeName> superTypesFor(final ExtendedTypeElement parent) {
        return StreamEx.of(this.parameterResolver.resolveParameters(parent));
    }

    protected Collection<TypeName> parentNamesOf(final ExtendedTypeElement baseElement) {
        return baseElement.conventionInterfaces()
                .flatMap(this::superTypesFor)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("parameterResolver", this.parameterResolver)
                .add("adapter", this.adapter)
                .toString();
    }
}
