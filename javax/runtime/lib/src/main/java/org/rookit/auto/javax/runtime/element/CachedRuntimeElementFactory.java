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
package org.rookit.auto.javax.runtime.element;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.rookit.auto.javax.runtime.RuntimeElementFactory;
import org.rookit.auto.javax.runtime.entity.RuntimeEntity;
import org.rookit.utils.guice.Uncached;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Element;
import java.util.Map;
import java.util.function.Supplier;

final class CachedRuntimeElementFactory implements RuntimeElementFactory {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(CachedRuntimeElementFactory.class);

    private final Map<RuntimeEntity, Element> createdElements;
    private final RuntimeElementFactory delegate;

    @Inject
    private CachedRuntimeElementFactory(@Uncached final RuntimeElementFactory delegate) {
        this.createdElements = Maps.newHashMap();
        this.delegate = delegate;
    }

    @Override
    public Element createFromSupplier(final Supplier<Element> supplier) {
        logger.trace("Creation through supplier is not memoized. Delegating");
        return this.delegate.createFromSupplier(supplier);
    }

    @Override
    public Element createFromEntity(final RuntimeEntity entity) {
        logger.trace("Fetching created element for entity '{}'", entity.name());
        return this.createdElements.computeIfAbsent(entity, this::delegateCreate);
    }

    private Element delegateCreate(final RuntimeEntity entity) {
        logger.trace("No element memoized. Delegating");
        return this.delegate.createFromEntity(entity);
    }

    @Override
    public String toString() {
        return "CachedRuntimeElementFactory{" +
                "createdElements=" + this.createdElements +
                ", delegate=" + this.delegate +
                "}";
    }

}
