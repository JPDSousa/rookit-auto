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

import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.entity.PropertyEntityFactory;
import org.rookit.auto.entity.cache.AbstractCacheEntityFactory;
import org.rookit.auto.guice.NoGeneric;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.identifier.IdentifierFactory;
import org.rookit.auto.javax.property.ExtendedPropertyEvaluator;
import org.rookit.auto.javax.property.PropertyFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;

import java.util.Collection;

public final class PropertyFlatEntityFactory extends AbstractCacheEntityFactory {

    public static EntityFactory create(final IdentifierFactory identifierFactory,
                                       final PartialEntityFactory partialEntityFactory,
                                       final SingleTypeSourceFactory typeSourceFactory,
                                       final PropertyEntityFactory propEntityFactory,
                                       final ExtendedPropertyEvaluator evaluator,
                                       final CodeSourceContainerFactory containerFactory,
                                       final PropertyFactory propertyFactory) {
        return new PropertyFlatEntityFactory(
                identifierFactory,
                partialEntityFactory,
                typeSourceFactory,
                propEntityFactory,
                evaluator, containerFactory, propertyFactory);
    }

    private final IdentifierFactory identifierFactory;
    private final PartialEntityFactory partialEntityFactory;
    private final SingleTypeSourceFactory singleTypeSourceFactory;
    private final PropertyEntityFactory propertyEntityFactory;
    private final ExtendedPropertyEvaluator filter;
    private final CodeSourceContainerFactory containerFactory;
    private final PropertyFactory propertyFactory;

    @Inject
    private PropertyFlatEntityFactory(final IdentifierFactory identifierFactory,
                                      @NoGeneric final PartialEntityFactory partialEntityFactory,
                                      final SingleTypeSourceFactory typeSourceFactory,
                                      final PropertyEntityFactory propEntityFactory,
                                      final ExtendedPropertyEvaluator filter,
                                      final CodeSourceContainerFactory containerFactory,
                                      final PropertyFactory propertyFactory) {
        this.identifierFactory = identifierFactory;
        this.partialEntityFactory = partialEntityFactory;
        this.singleTypeSourceFactory = typeSourceFactory;
        this.propertyEntityFactory = propEntityFactory;
        this.filter = filter;
        this.containerFactory = containerFactory;
        this.propertyFactory = propertyFactory;
    }

    @Override
    protected Entity createNew(final ExtendedTypeElement element) {
        final Identifier identifier = this.identifierFactory.create(element);
        final TypeSource source = this.singleTypeSourceFactory.create(identifier, element);

        final Collection<Entity> entities = StreamEx.of(element.properties())
                .filter(this.filter)
                .map(this.propertyEntityFactory::create)
                .toImmutableSet();

        return new PropertyFlatEntity(identifier, this.containerFactory.create(entities), source,
                this.partialEntityFactory.create(element));
    }

    @Override
    public String toString() {
        return "PropertyFlatEntityFactory{" +
                "identifierFactory=" + this.identifierFactory +
                ", partialEntityFactory=" + this.partialEntityFactory +
                ", singleTypeSourceFactory=" + this.singleTypeSourceFactory +
                ", propertyEntityFactory=" + this.propertyEntityFactory +
                ", filter=" + this.filter +
                ", containerFactory=" + this.containerFactory +
                ", propertyFactory=" + this.propertyFactory +
                "} " + super.toString();
    }
}
