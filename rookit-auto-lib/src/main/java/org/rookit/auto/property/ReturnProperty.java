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

import org.rookit.auto.javax.element.ElementUtils;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;
import org.rookit.auto.javax.property.AbstractExtendedProperty;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.utils.repetition.Repetition;

import javax.lang.model.type.TypeMirror;

final class ReturnProperty extends AbstractExtendedProperty {

    private final ExtendedProperty source;
    private final TypeMirror returnType;
    private final Repetition repetition;

    ReturnProperty(final ExtendedProperty source,
                   final Repetition repetition,
                   final TypeMirror returnType,
                   final ElementUtils utils,
                   final ExtendedTypeElementFactory elementFactory) {
        super(utils, elementFactory);
        this.source = source;
        this.repetition = repetition;
        this.returnType = returnType;
    }

    @Override
    public String name() {
        return this.source.name();
    }

    @Override
    public TypeMirror type() {
        return this.returnType;
    }

    @Override
    public boolean isFinal() {
        return this.source.isFinal();
    }

    @Override
    public Repetition repetition() {
        return this.repetition;
    }

    @Override
    public String toString() {
        return "ReturnProperty{" +
                "source=" + this.source +
                ", returnType=" + this.returnType +
                ", repetition=" + this.repetition +
                "} " + super.toString();
    }
}
