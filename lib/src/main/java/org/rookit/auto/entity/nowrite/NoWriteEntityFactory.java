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
package org.rookit.auto.entity.nowrite;

import com.google.inject.Inject;
import org.rookit.auto.entity.cache.AbstractCacheEntityFactory;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.utils.primitive.VoidUtils;

public final class NoWriteEntityFactory extends AbstractCacheEntityFactory {

    public static EntityFactory create(final EntityFactory delegate,
                                       final VoidUtils voidUtils) {
        return new NoWriteEntityFactory(delegate, voidUtils);
    }

    private final EntityFactory delegate;
    private final VoidUtils voidUtils;

    @Inject
    private NoWriteEntityFactory(final EntityFactory delegate, final VoidUtils voidUtils) {
        this.delegate = delegate;
        this.voidUtils = voidUtils;
    }

    @Override
    protected Entity createNew(final ExtendedTypeElement element) {
        return new NoWriteEntity(this.delegate.create(element), this.voidUtils);
    }

    @Override
    public String toString() {
        return "NoWriteEntityFactory{" +
                "delegate=" + this.delegate +
                ", voidUtils=" + this.voidUtils +
                "}";
    }
}
