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
package org.rookit.auto.property;

import com.google.common.base.MoreObjects;
import org.rookit.auto.javax.ExtendedTypeMirror;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;
import org.rookit.auto.javax.property.AbstractExtendedProperty;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.utils.repetition.Repetition;

final class NameProperty extends AbstractExtendedProperty {

    private final String name;
    private final ExtendedProperty property;

    NameProperty(final ElementUtils utils,
                 final String name,
                 final ExtendedProperty property,
                 final ExtendedTypeElementFactory elementFactory) {
        super(utils, elementFactory);
        this.name = name;
        this.property = property;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public ExtendedTypeMirror type() {
        return this.property.type();
    }

    @Override
    public boolean isFinal() {
        return this.property.isFinal();
    }

    @Override
    public Repetition repetition() {
        return this.property.repetition();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", this.name)
                .add("property", this.property)
                .toString();
    }
}
