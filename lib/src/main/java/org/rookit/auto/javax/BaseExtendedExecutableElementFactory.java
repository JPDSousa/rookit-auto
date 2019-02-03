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

import com.google.inject.Inject;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;

import static java.lang.String.format;

final class BaseExtendedExecutableElementFactory implements ExtendedExecutableElementFactory {

    private final ExtendedTypeMirrorFactory typeMirrorFactory;
    private final Messager messager;

    @Inject
    private BaseExtendedExecutableElementFactory(final ExtendedTypeMirrorFactory typeMirrorFactory,
                                                 final Messager messager) {
        this.typeMirrorFactory = typeMirrorFactory;
        this.messager = messager;
    }

    @Override
    public ExtendedExecutableElement create(final ExecutableElement executableElement) {
        if (executableElement instanceof ExtendedExecutableElement) {
            final String errMsg = format("%s is already a %s. Bypassing creation.", executableElement,
                    ExtendedExecutableElement.class.getName());
            this.messager.printMessage(Diagnostic.Kind.NOTE, errMsg);
            return (ExtendedExecutableElement) executableElement;
        }
        return new ExtendedExecutableElementImpl(executableElement, this.typeMirrorFactory);
    }

    @Override
    public String toString() {
        return "BaseExtendedExecutableElementFactory{" +
                "typeMirrorFactory=" + this.typeMirrorFactory +
                ", messager=" + this.messager +
                "}";
    }
}
