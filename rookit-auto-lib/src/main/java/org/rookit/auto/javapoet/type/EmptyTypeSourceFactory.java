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
package org.rookit.auto.javapoet.type;

import com.google.common.base.MoreObjects;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.entity.Identifier;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.naming.NamingFactory;
import org.rookit.auto.naming.PackageReference;
import org.rookit.auto.source.SingleTypeSourceFactory;
import org.rookit.auto.source.TypeSource;

import javax.lang.model.element.Modifier;

public final class EmptyTypeSourceFactory implements SingleTypeSourceFactory {

    public static SingleTypeSourceFactory create(final NamingFactory namingFactory) {
        return new EmptyTypeSourceFactory(namingFactory);
    }

    private final NamingFactory namingFactory;

    private EmptyTypeSourceFactory(final NamingFactory namingFactory) {
        this.namingFactory = namingFactory;
    }

    @Override
    public TypeSource create(final Identifier identifier,
                             final ExtendedTypeElement element) {
        final PackageReference packageReference = this.namingFactory.packageName(element);
        final String className = this.namingFactory.type(element);
        final TypeSpec spec = TypeSpec.interfaceBuilder(ClassName.get(packageReference.fullName(), className))
                .addModifiers(Modifier.PUBLIC)
                .build();
        return new BaseTypeSource(identifier, spec);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("namingFactory", this.namingFactory)
                .toString();
    }
}
