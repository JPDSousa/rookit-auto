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
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javapoet.SingleTypeSpecFactory;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactory;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;

public abstract class AbstractJavaPoetEntityFactory extends AbstractCacheJavaPoetEntityFactory {

    private final JavaPoetPartialEntityFactory partialEntityFactory;
    private final JavaPoetEntityFactory noopFactory;
    private final JavaPoetIdentifierFactory identifierFactory;
    private final JavaPoetNamingFactory namingFactory;
    private final SingleTypeSpecFactory typeSpecFactory;

    protected AbstractJavaPoetEntityFactory(final JavaPoetPartialEntityFactory partialEntityFactory,
                                            final JavaPoetEntityFactory noopFactory,
                                            final JavaPoetIdentifierFactory identifierFactory,
                                            final JavaPoetNamingFactory namingFactory,
                                            final SingleTypeSpecFactory typeSpecFactory) {
        this.partialEntityFactory = partialEntityFactory;
        this.noopFactory = noopFactory;
        this.identifierFactory = identifierFactory;
        this.namingFactory = namingFactory;
        this.typeSpecFactory = typeSpecFactory;
    }

    protected abstract boolean contains(ExtendedTypeElement element);

    @Override
    JavaPoetEntity createNew(final ExtendedTypeElement element) {
        if (contains(element)) {
            return this.noopFactory.create(element);
        }
        final JavaPoetPartialEntity partialEntity = this.partialEntityFactory.create(element);
        final JavaPoetIdentifier identifier = this.identifierFactory.create(element);
        final TypeName parent = this.namingFactory.resolveParameters(element);
        final TypeSpec source = this.typeSpecFactory.create(identifier, element,
                ImmutableSet.of(parent));

        return new JavaPoetEntityImpl(partialEntity, identifier, source);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("partialEntityFactory", this.partialEntityFactory)
                .add("noopFactory", this.noopFactory)
                .add("identifierFactory", this.identifierFactory)
                .add("namingFactory", this.namingFactory)
                .add("typeSpecFactory", this.typeSpecFactory)
                .toString();
    }
}
