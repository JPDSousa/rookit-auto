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
package org.rookit.auto.javapoet.typespec;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.javapoet.SingleTypeSpecFactory;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;

import javax.lang.model.element.Modifier;
import java.util.Collection;

public final class EmptyTypeSpecFactory implements SingleTypeSpecFactory {

    public static SingleTypeSpecFactory create() {
        return new EmptyTypeSpecFactory();
    }

    private EmptyTypeSpecFactory() { }

    @Override
    public TypeSpec create(final JavaPoetIdentifier identifier,
                           final ExtendedTypeElement element,
                           final Collection<TypeName> superTypes) {
        return TypeSpec.interfaceBuilder(identifier.className())
                .addSuperinterfaces(superTypes)
                .addModifiers(Modifier.PUBLIC)
                .build();
    }
}
