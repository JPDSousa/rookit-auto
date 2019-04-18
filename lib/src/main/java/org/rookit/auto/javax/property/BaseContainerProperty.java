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

import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.utils.repetition.Repetition;

final class BaseContainerProperty implements ContainerProperty {

    private final Property baseProperty;
    private final ExtendedTypeElement typeMirrorAsElement;

    BaseContainerProperty(final Property baseProperty, final ExtendedTypeElement typeMirrorAsElement) {
        this.baseProperty = baseProperty;
        this.typeMirrorAsElement = typeMirrorAsElement;
    }

    @Override
    public ExtendedTypeElement typeAsElement() {
        return this.typeMirrorAsElement;
    }

    @Override
    public String name() {
        return this.baseProperty.name();
    }

    @Override
    public ExtendedTypeMirror type() {
        return this.baseProperty.type();
    }

    @Override
    public boolean isFinal() {
        return this.baseProperty.isFinal();
    }

    @Override
    public Repetition repetition() {
        return this.baseProperty.repetition();
    }
}
