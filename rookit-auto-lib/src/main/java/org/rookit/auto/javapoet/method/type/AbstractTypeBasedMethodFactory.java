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
package org.rookit.auto.javapoet.method.type;

import com.google.common.base.MoreObjects;
import org.rookit.auto.javapoet.method.TypeBasedMethodFactory;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.property.ExtendedProperty;

public abstract class AbstractTypeBasedMethodFactory implements TypeBasedMethodFactory {

    private final ElementUtils utils;

    protected AbstractTypeBasedMethodFactory(final ElementUtils types) {
        this.utils = types;
    }

    protected ElementUtils utils() {
        return this.utils;
    }

    @Override
    public boolean isCompatible(final ExtendedProperty property) {
        return this.utils.isSameType(type(), property.type());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("utils", this.utils)
                .toString();
    }
}
