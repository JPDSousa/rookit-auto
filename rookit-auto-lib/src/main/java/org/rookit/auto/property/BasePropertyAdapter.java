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

import com.google.inject.Inject;
import org.rookit.auto.javax.JavaxRepetitionFactory;
import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyAdapter;
import org.rookit.utils.repetition.Repetition;

import javax.lang.model.type.TypeMirror;

public final class BasePropertyAdapter implements PropertyAdapter {

    public static PropertyAdapter create(final ElementUtils utils,
                                         final JavaxRepetitionFactory repetitionFactory,
                                         final ExtendedTypeElementFactory elementFactory) {
        return new BasePropertyAdapter(utils, repetitionFactory, elementFactory);
    }

    private final ElementUtils utils;
    private final JavaxRepetitionFactory repetitionFactory;
    private final ExtendedTypeElementFactory elementFactory;

    @Inject
    private BasePropertyAdapter(final ElementUtils utils,
                                final JavaxRepetitionFactory repetitionFactory,
                                final ExtendedTypeElementFactory elementFactory) {
        this.utils = utils;
        this.repetitionFactory = repetitionFactory;
        this.elementFactory = elementFactory;
    }

    @Override
    public ExtendedProperty changeReturnType(final ExtendedProperty source, final TypeMirror newReturnType) {
        final Repetition repetition = this.repetitionFactory.fromTypeMirror(newReturnType);
        return new ReturnProperty(source, repetition, newReturnType, this.utils,
                this.elementFactory);
    }

    @Override
    public ExtendedProperty changeName(final ExtendedProperty source, final String newName) {
        return new NameProperty(this.utils, newName, source, this.elementFactory);
    }

    @Override
    public String toString() {
        return "BasePropertyAdapter{" +
                "utils=" + this.utils +
                ", repetitionFactory=" + this.repetitionFactory +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
