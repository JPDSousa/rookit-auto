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
package org.rookit.auto.entity.property;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PropertyEntityFactory;
import org.rookit.auto.entity.PropertyPartialEntityFactory;
import org.rookit.auto.identifier.PropertyIdentifierFactory;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.PropertyTypeSourceFactory;

import java.util.Collection;

public final class BasePropertyEntityFactory implements PropertyEntityFactory {

    public static PropertyEntityFactory create(final PropertyIdentifierFactory identifierFactory,
                                               final PropertyTypeSourceFactory typeSourceFactory,
                                               final Provider<EntityFactory> entityFactory,
                                               final PropertyPartialEntityFactory partialEntityFactory) {
        return new BasePropertyEntityFactory(identifierFactory, typeSourceFactory, entityFactory, partialEntityFactory);
    }

    private final PropertyIdentifierFactory identifierFactory;
    private final PropertyTypeSourceFactory typeSourceFactory;
    private final Provider<EntityFactory> entityFactoryProvider;
    private final PropertyPartialEntityFactory partialEntityFactory;

    @Inject
    private BasePropertyEntityFactory(final PropertyIdentifierFactory identifierFactory,
                                      final PropertyTypeSourceFactory typeSourceFactory,
                                      final Provider<EntityFactory> entityFactory,
                                      final PropertyPartialEntityFactory partialEntityFactory) {
        this.identifierFactory = identifierFactory;
        this.typeSourceFactory = typeSourceFactory;
        this.entityFactoryProvider = entityFactory;
        this.partialEntityFactory = partialEntityFactory;
    }

    @Override
    public Entity create(final ExtendedProperty property) {
        final EntityFactory entityFactory = this.entityFactoryProvider.get();

        final Collection<Entity> children = property.typeAsElement()
                .filter(ExtendedTypeElement::isPropertyContainer)
                .map(entityFactory::create)
                .map(ImmutableList::of)
                .orElse(ImmutableList.of());

        return new PropertyFlatEntity(
                this.identifierFactory.create(property),
                children,
                this.typeSourceFactory.create(property),
                this.partialEntityFactory.create(property));
    }

}
