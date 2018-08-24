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
package org.rookit.auto.entity;

import com.google.common.base.MoreObjects;
import org.rookit.auto.identifier.IdentifierFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;

public abstract class AbstractEntityFactory extends AbstractCacheEntityFactory {

    private final PartialEntityFactory partialEntityFactory;
    private final EntityFactory noopFactory;
    private final IdentifierFactory identifierFactory;
    private final SingleTypeSourceFactory typeSpecFactory;

    protected AbstractEntityFactory(final PartialEntityFactory partialEntityFactory,
                                    final EntityFactory noopFactory,
                                    final IdentifierFactory identifierFactory,
                                    final SingleTypeSourceFactory typeSpecFactory) {
        this.partialEntityFactory = partialEntityFactory;
        this.noopFactory = noopFactory;
        this.identifierFactory = identifierFactory;
        this.typeSpecFactory = typeSpecFactory;
    }

    protected abstract boolean contains(ExtendedTypeElement element);

    @Override
    Entity createNew(final ExtendedTypeElement element) {
        if (contains(element)) {
            return this.noopFactory.create(element);
        }
        final PartialEntity partialEntity = this.partialEntityFactory.create(element);
        final Identifier identifier = this.identifierFactory.create(element);
        final TypeSource source = this.typeSpecFactory.create(identifier, element);

        return new EntityImpl(partialEntity, identifier, source);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("partialEntityFactory", this.partialEntityFactory)
                .add("noopFactory", this.noopFactory)
                .add("identifierFactory", this.identifierFactory)
                .add("typeSpecFactory", this.typeSpecFactory)
                .toString();
    }
}
