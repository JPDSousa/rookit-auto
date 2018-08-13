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
package org.rookit.auto.javapoet.entity;

import com.google.common.base.MoreObjects;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javapoet.SingleTypeSpecFactory;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactory;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractJavaPoetPartialEntityFactory implements JavaPoetPartialEntityFactory {

    private final JavaPoetPartialEntityFactory noopFactory;
    private final JavaPoetIdentifierFactory identifierFactory;
    private final SingleTypeSpecFactory typeSpecFactory;

    protected AbstractJavaPoetPartialEntityFactory(final JavaPoetPartialEntityFactory noopFactory,
                                                   final JavaPoetIdentifierFactory identifierFactory,
                                                   final SingleTypeSpecFactory typeSpecFactory) {
        this.noopFactory = noopFactory;
        this.identifierFactory = identifierFactory;
        this.typeSpecFactory = typeSpecFactory;
    }

    protected abstract boolean contains(ExtendedTypeElement element);

    @Override
    public JavaPoetPartialEntity create(final ExtendedTypeElement element) {
        if (contains(element)) {
            return this.noopFactory.create(element);
        }
        final JavaPoetIdentifier identifier = this.identifierFactory.create(element);
        final Collection<JavaPoetPartialEntity> parents = createParentsOf(element);
        final Collection<TypeName> interfaceNames = parentNamesOf(element);
        final TypeSpec source = this.typeSpecFactory.create(identifier, element, interfaceNames);

        return new JavaPoetPartialEntityImpl(identifier, parents, source);
    }

    private Collection<JavaPoetPartialEntity> createParentsOf(final ExtendedTypeElement baseElement) {
        return baseElement.conventionInterfaces()
                .flatMap(this::entitiesFor)
                .collect(Collectors.toSet());
    }

    protected abstract StreamEx<JavaPoetPartialEntity> entitiesFor(ExtendedTypeElement parent);


    protected abstract StreamEx<TypeName> superTypesFor(ExtendedTypeElement parent);

    protected Collection<TypeName> parentNamesOf(final ExtendedTypeElement baseElement) {
        return baseElement.conventionInterfaces()
                .flatMap(this::superTypesFor)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("noopFactory", this.noopFactory)
                .add("identifierFactory", this.identifierFactory)
                .add("typeSpecFactory", this.typeSpecFactory)
                .toString();
    }
}
