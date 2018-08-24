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
package org.rookit.auto.entity.noop;

import com.google.common.base.MoreObjects;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.Identifier;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.identifier.IdentifierFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public final class NoopEntityFactory implements EntityFactory {

    public static EntityFactory create(final Messager messager,
                                       final IdentifierFactory identifierFactory,
                                       final PartialEntityFactory partialEntityFactory) {
        return new NoopEntityFactory(messager, identifierFactory, partialEntityFactory);
    }

    private final Messager messager;
    private final IdentifierFactory identifierFactory;
    private final PartialEntityFactory partialEntityFactory;

    private NoopEntityFactory(final Messager messager,
                              final IdentifierFactory identifierFactory,
                              final PartialEntityFactory partialEntityFactory) {
        this.messager = messager;
        this.identifierFactory = identifierFactory;
        this.partialEntityFactory = partialEntityFactory;
    }

    @Override
    public Entity create(final ExtendedTypeElement element) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, "Excluding, as already exist: " + element.getSimpleName());
        final Identifier identifier = this.identifierFactory.create(element);
        final PartialEntity partialEntity = this.partialEntityFactory.create(element);
        return new ExclusionEntity(identifier, partialEntity);
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
