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
package org.rookit.auto.entity.propertyflat;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.rookit.auto.entity.AbstractCacheEntityFactory;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.entity.PropertyEntityFactory;
import org.rookit.auto.identifier.EntityIdentifierFactory;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javax.property.PropertyEvaluator;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;
import org.rookit.utils.optional.OptionalFactory;

import java.util.Collection;
import java.util.Set;

public final class PropertFlatEntityFactory extends AbstractCacheEntityFactory {

    public static EntityFactory create(final EntityIdentifierFactory identifierFactory,
                                       final Provider<PartialEntityFactory> partialEntityFactory,
                                       final SingleTypeSourceFactory typeSourceFactory,
                                       final PropertyEntityFactory propEntityFactory,
                                       final OptionalFactory optionalFactory, PropertyEvaluator evaluator) {
        return new PropertFlatEntityFactory(identifierFactory, partialEntityFactory, typeSourceFactory,
                propEntityFactory, optionalFactory, evaluator);
    }

    private final EntityIdentifierFactory identifierFactory;
    private final Provider<PartialEntityFactory> partialEntityFactory;
    private final SingleTypeSourceFactory singleTypeSourceFactory;
    private final PropertyEntityFactory propertyEntityFactory;
    private final OptionalFactory optionalFactory;
    private final PropertyEvaluator filter;

    @Inject
    private PropertFlatEntityFactory(final EntityIdentifierFactory identifierFactory,
                                     final Provider<PartialEntityFactory> partialEntityFactory,
                                     final SingleTypeSourceFactory typeSourceFactory,
                                     final PropertyEntityFactory propEntityFactory,
                                     final OptionalFactory optionalFactory,
                                     final PropertyEvaluator filter) {
        this.identifierFactory = identifierFactory;
        this.partialEntityFactory = partialEntityFactory;
        this.singleTypeSourceFactory = typeSourceFactory;
        this.propertyEntityFactory = propEntityFactory;
        this.optionalFactory = optionalFactory;
        this.filter = filter;
    }

    @Override
    protected Entity createNew(final ExtendedTypeElement element) {
        final PartialEntityFactory partialEntityFactory = this.partialEntityFactory.get();
        final Set<PartialEntity> parents = element.conventionInterfaces()
                .map(partialEntityFactory::create)
                .toSet();
        final Identifier identifier = this.identifierFactory.create(element);
        final Collection<Entity> entities = Lists.newArrayList();

        final TypeSource source = this.singleTypeSourceFactory.create(identifier, element);
        element.properties().stream()
                .filter(this.filter)
                .map(this.propertyEntityFactory::create)
                .forEach(entities::add);

        return new PropertyFlatEntity(parents, identifier, entities, source, this.optionalFactory);
    }

    @Override
    public String toString() {
        return "PropertFlatEntityFactory{" +
                "identifierFactory=" + this.identifierFactory +
                ", partialEntityFactory=" + this.partialEntityFactory +
                ", singleTypeSourceFactory=" + this.singleTypeSourceFactory +
                ", propertyEntityFactory=" + this.propertyEntityFactory +
                ", optionalFactory=" + this.optionalFactory +
                ", filter=" + this.filter +
                "} " + super.toString();
    }
}
