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

import javax.annotation.processing.Messager;

import static java.lang.String.format;
import static javax.tools.Diagnostic.Kind.WARNING;

abstract class AbstractRepetition<R extends RepetitiveTypeMirror> implements JavaxRepetition {

    private final String name;
    private final Messager messager;
    private final boolean multi;
    private final boolean optional;
    private final R repetitiveTypeMirror;

    AbstractRepetition(final Messager messager,
                       final boolean optional,
                       final R repetitiveTypeMirror,
                       final boolean multi,
                       final String name) {
        this.messager = messager;
        this.optional = optional;
        this.repetitiveTypeMirror = repetitiveTypeMirror;
        this.multi = multi;
        this.name = name;
    }

    @Override
    public ExtendedTypeMirror unwrap(final ExtendedTypeMirror wrappedType) {
        if (this.repetitiveTypeMirror.isSameTypeErasure(wrappedType)) {
            return this.repetitiveTypeMirror.unwrap(wrappedType);
        }
        final String message = format("Type %s was not unwrapped because is not valid for repetition type %s",
                wrappedType, this.name);
        this.messager.printMessage(WARNING, message);
        return wrappedType;
    }

    @Override
    public boolean isMulti() {
        return this.multi;
    }

    @Override
    public boolean isOptional() {
        return this.optional;
    }

    R repetitiveTypeMirror() {
        return this.repetitiveTypeMirror;
    }

    @Override
    public String toString() {
        return "BaseRepetition{" +
                "name='" + this.name + '\'' +
                ", messager=" + this.messager +
                ", multi=" + this.multi +
                ", optional=" + this.optional +
                ", repetitiveTypeMirror=" + this.repetitiveTypeMirror +
                "}";
    }
}
