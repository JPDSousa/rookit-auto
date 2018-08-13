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
package org.rookit.auto.javax;

import com.google.common.base.MoreObjects;
import org.rookit.auto.javax.element.ElementUtils;

import javax.lang.model.type.TypeMirror;

public final class BaseExtendedPropertyFactory implements ExtendedPropertyFactory {

    public static ExtendedPropertyFactory create(final ElementUtils utils) {
        return new BaseExtendedPropertyFactory(utils);
    }

    private final ElementUtils utils;

    private BaseExtendedPropertyFactory(final ElementUtils utils) {
        this.utils = utils;
    }

    @Override
    public ExtendedProperty create(final String name, final TypeMirror type) {
        return new BaseExtendedProperty(name, type, this.utils);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("utils", this.utils)
                .toString();
    }
}
