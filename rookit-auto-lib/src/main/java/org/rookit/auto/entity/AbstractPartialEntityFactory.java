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

import one.util.streamex.StreamEx;
import org.rookit.auto.identifier.EntityIdentifierFactory;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;
import org.rookit.utils.optional.OptionalFactory;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractPartialEntityFactory implements PartialEntityFactory {

    private final EntityIdentifierFactory identifierFactory;
    private final SingleTypeSourceFactory typeSourceFactory;
    private final OptionalFactory optionalFactory;

    protected AbstractPartialEntityFactory(final EntityIdentifierFactory identifierFactory,
                                           final SingleTypeSourceFactory typeSourceFactory,
                                           final OptionalFactory optionalFactory) {
        this.identifierFactory = identifierFactory;
        this.typeSourceFactory = typeSourceFactory;
        this.optionalFactory = optionalFactory;
    }

    @Override
    public PartialEntity create(final ExtendedTypeElement element) {
        final Identifier identifier = this.identifierFactory.create(element);
        final Collection<PartialEntity> parents = createParentsOf(element);
        final TypeSource source = this.typeSourceFactory.create(identifier, element);

        return new PartialEntityImpl(identifier, parents, source, this.optionalFactory);
    }

    private Collection<PartialEntity> createParentsOf(final ExtendedTypeElement baseElement) {
        return baseElement.conventionInterfaces()
                .flatMap(this::entitiesFor)
                .collect(Collectors.toSet());
    }

    protected StreamEx<PartialEntity> entitiesFor(final ExtendedTypeElement parent) {
        return StreamEx.of(create(parent));
    }

    @Override
    public String toString() {
        return "AbstractPartialEntityFactory{" +
                ", identifierFactory=" + this.identifierFactory +
                ", typeSourceFactory=" + this.typeSourceFactory +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
