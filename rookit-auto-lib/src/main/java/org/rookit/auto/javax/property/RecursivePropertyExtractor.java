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
package org.rookit.auto.javax.property;

import com.google.inject.Inject;
import one.util.streamex.StreamEx;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.utils.optional.Optional;

import javax.lang.model.element.TypeElement;
import java.util.stream.Stream;

public final class RecursivePropertyExtractor implements PropertyExtractor {

    public static PropertyExtractor create(final PropertyExtractor delegate,
                                           final ElementUtils utils) {
        return new RecursivePropertyExtractor(delegate, utils);
    }

    private final PropertyExtractor delegate;
    private final ElementUtils utils;

    @Inject
    private RecursivePropertyExtractor(final PropertyExtractor delegate,
                                       final ElementUtils utils) {
        this.delegate = delegate;
        this.utils = utils;
    }

    @Override
    public Stream<ExtendedProperty> fromType(final TypeElement element) {
        // TODO copied from AbstractTypeElementDecorator#conventionInterfaces
        return StreamEx.of(element.getInterfaces())
                .map(this.utils::toElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .select(TypeElement.class)
                .filter(this.utils::isConventionElement)
                .flatMap(this::fromType)
                .append(this.delegate.fromType(element));
    }

    @Override
    public String toString() {
        return "RecursivePropertyExtractor{" +
                "delegate=" + this.delegate +
                ", utils=" + this.utils +
                "}";
    }
}
