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
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javapoet.entity.JavaPoetEntity;
import org.rookit.auto.javapoet.entity.JavaPoetEntityFactory;
import org.rookit.auto.javapoet.entity.JavaPoetPartialEntity;
import org.rookit.auto.javapoet.entity.JavaPoetPartialEntityFactory;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactory;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public final class NoopJavaPoetEntityFactory implements JavaPoetEntityFactory {

    public static JavaPoetEntityFactory create(final Messager messager,
                                               final JavaPoetIdentifierFactory identifierFactory,
                                               final JavaPoetPartialEntityFactory partialEntityFactory) {
        return new NoopJavaPoetEntityFactory(messager, identifierFactory, partialEntityFactory);
    }

    private final Messager messager;
    private final JavaPoetIdentifierFactory identifierFactory;
    private final JavaPoetPartialEntityFactory partialEntityFactory;

    private NoopJavaPoetEntityFactory(final Messager messager,
                                      final JavaPoetIdentifierFactory identifierFactory,
                                      final JavaPoetPartialEntityFactory partialEntityFactory) {
        this.messager = messager;
        this.identifierFactory = identifierFactory;
        this.partialEntityFactory = partialEntityFactory;
    }

    @Override
    public JavaPoetEntity create(final ExtendedTypeElement element) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, "Excluding, as already exist: " + element.getSimpleName());
        final JavaPoetIdentifier identifier = this.identifierFactory.create(element);
        final JavaPoetPartialEntity partialEntity = this.partialEntityFactory.create(element);
        return new ExclusionJavaPoetEntity(identifier, partialEntity);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messager", this.messager)
                .add("identifierFactory", this.identifierFactory)
                .add("partialEntityFactory", this.partialEntityFactory)
                .toString();
    }
}
