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

import org.rookit.auto.javax.type.ElementUtils;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeElementFactory;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.repetition.Repetition;

import javax.lang.model.element.TypeElement;

final class BaseExtendedProperty extends AbstractProperty implements ExtendedProperty {

    private final Property source;
    private final ElementUtils utils;
    @SuppressWarnings("FieldNotUsedInToString") // to avoid stack overflow
    private final ExtendedTypeElementFactory elementFactory;

    BaseExtendedProperty(final Property source,
                         final ElementUtils utils,
                         final ExtendedTypeElementFactory elementFactory) {
        this.source = source;
        this.utils = utils;
        this.elementFactory = elementFactory;
    }

    @Override
    public String name() {
        return this.source.name();
    }

    @Override
    public ExtendedTypeMirror type() {
        return this.source.type();
    }

    @Override
    public boolean isFinal() {
        return this.source.isFinal();
    }

    @Override
    public Repetition repetition() {
        return this.source.repetition();
    }

    @Override
    public Optional<ExtendedTypeElement> typeAsElement() {
        return type().toElement()
                .map(element -> (TypeElement) element)
                .map(this.elementFactory::extend);
    }

    @Override
    public boolean isContainer() {
        return this.source.isContainer();
    }

    @Override
    public String toString() {
        return "BaseExtendedProperty{" +
                "source=" + this.source +
                ", utils=" + this.utils +
                "} ";
    }
}
