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
package org.rookit.auto.entity.parent;

import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.javax.element.ExtendedTypeElement;

public final class MultiFactoryParentExtractor implements ParentExtractor {

    public static ParentExtractor create(final PartialEntityFactory elementFactory,
                                         final PartialEntityFactory childFactory) {
        return new MultiFactoryParentExtractor(elementFactory, childFactory);
    }

    private final PartialEntityFactory elementFactory;
    private final PartialEntityFactory childFactory;

    @Inject
    private MultiFactoryParentExtractor(final PartialEntityFactory elementFactory,
                                        final PartialEntityFactory childFactory) {
        this.elementFactory = elementFactory;
        this.childFactory = childFactory;
    }

    @Override
    public StreamEx<PartialEntity> extractFrom(final ExtendedTypeElement element) {
        return element.conventionInterfaces()
                .flatMap(this::entitiesFor);
    }

    private StreamEx<PartialEntity> entitiesFor(final ExtendedTypeElement parent) {
        return parent.child()
                .map(child -> StreamEx.of(this.childFactory.create(child), this.elementFactory.create(parent)))
                .orElseGet(() -> StreamEx.of(this.elementFactory.create(parent)));
    }

    @Override
    public String toString() {
        return "MultiFactoryParentExtractor{" +
                "elementFactory=" + this.elementFactory +
                ", childFactory=" + this.childFactory +
                "}";
    }
}
