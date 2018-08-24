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
import one.util.streamex.StreamEx;
import org.rookit.auto.identifier.IdentifierFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractPartialEntityFactory implements PartialEntityFactory {

    private final PartialEntityFactory noopFactory;
    private final IdentifierFactory identifierFactory;
    private final SingleTypeSourceFactory typeSourceFactory;

    protected AbstractPartialEntityFactory(final PartialEntityFactory noopFactory,
                                           final IdentifierFactory identifierFactory,
                                           final SingleTypeSourceFactory typeSourceFactory) {
        this.noopFactory = noopFactory;
        this.identifierFactory = identifierFactory;
        this.typeSourceFactory = typeSourceFactory;
    }

    protected abstract boolean contains(ExtendedTypeElement element);

    @Override
    public PartialEntity create(final ExtendedTypeElement element) {
        if (contains(element)) {
            return this.noopFactory.create(element);
        }
        final Identifier identifier = this.identifierFactory.create(element);
        final Collection<PartialEntity> parents = createParentsOf(element);
        final TypeSource source = this.typeSourceFactory.create(identifier, element);

        return new PartialEntityImpl(identifier, parents, source);
    }

    private Collection<PartialEntity> createParentsOf(final ExtendedTypeElement baseElement) {
        return baseElement.conventionInterfaces()
                .flatMap(this::entitiesFor)
                .collect(Collectors.toSet());
    }

    protected abstract StreamEx<PartialEntity> entitiesFor(ExtendedTypeElement parent);

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("noopFactory", this.noopFactory)
                .add("identifierFactory", this.identifierFactory)
                .add("typeSourceFactory", this.typeSourceFactory)
                .toString();
    }
}