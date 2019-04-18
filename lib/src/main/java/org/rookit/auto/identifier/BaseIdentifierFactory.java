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
package org.rookit.auto.identifier;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifierFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.naming.NamingFactory;
import org.rookit.auto.naming.PackageReference;

import java.util.Map;

public final class BaseIdentifierFactory implements JavaPoetIdentifierFactory {

    public static JavaPoetIdentifierFactory create(final NamingFactory namingFactory) {
        return new BaseIdentifierFactory(namingFactory);
    }

    private final NamingFactory namingFactory;
    private final Map<String, JavaPoetIdentifier> cache;

    @Inject
    private BaseIdentifierFactory(final NamingFactory namingFactory) {
        this.namingFactory = namingFactory;
        this.cache = Maps.newHashMap();
    }

    @Override
    public JavaPoetIdentifier create(final ExtendedTypeElement typeElement) {
        return this.cache.computeIfAbsent(typeElement.getQualifiedName().toString(), name -> createNew(typeElement));
    }

    @Override
    public JavaPoetIdentifier create(final PackageReference packageReference) {
        return this.cache.computeIfAbsent(packageReference.fullName(), name -> createNew(packageReference));
    }

    private JavaPoetIdentifier createNew(final PackageReference packageReference) {
        return new EntityIdentifier(this.namingFactory.packageName(packageReference),
                this.namingFactory.type(packageReference));
    }

    private JavaPoetIdentifier createNew(final ExtendedTypeElement typeElement) {
        final String className = this.namingFactory.type(typeElement);
        final PackageReference packageReference = this.namingFactory.packageName(typeElement);
        return new EntityIdentifier(packageReference, className);
    }

    @Override
    public String toString() {
        return "BaseIdentifierFactory{" +
                "namingFactory=" + this.namingFactory +
                ", cache=" + this.cache +
                "}";
    }
}
