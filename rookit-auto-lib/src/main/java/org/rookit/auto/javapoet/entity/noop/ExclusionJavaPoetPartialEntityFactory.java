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
package org.rookit.auto.javapoet.entity.noop;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javapoet.entity.JavaPoetPartialEntity;
import org.rookit.auto.javapoet.entity.JavaPoetPartialEntityFactory;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactory;

public final class ExclusionJavaPoetPartialEntityFactory implements JavaPoetPartialEntityFactory {

    public static JavaPoetPartialEntityFactory create(final JavaPoetIdentifierFactory identifierFactory) {
        return new ExclusionJavaPoetPartialEntityFactory(identifierFactory);
    }

    private final JavaPoetIdentifierFactory identifierFactory;

    private ExclusionJavaPoetPartialEntityFactory(final JavaPoetIdentifierFactory identifierFactory) {
        this.identifierFactory = identifierFactory;
    }

    @Override
    public JavaPoetPartialEntity create(final ExtendedTypeElement element) {
        final JavaPoetIdentifier identifier = this.identifierFactory.create(element);
        return new ExclusionJavaPoetPartialEntity(identifier, ImmutableSet.of());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("identifierFactory", this.identifierFactory)
                .toString();
    }
}
