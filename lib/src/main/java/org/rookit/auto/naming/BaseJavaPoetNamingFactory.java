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
package org.rookit.auto.naming;

import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javax.type.ExtendedTypeElement;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.text.WordUtils.capitalize;

public final class BaseJavaPoetNamingFactory implements JavaPoetNamingFactory {

    public static JavaPoetNamingFactory create(final PackageReference packageReference,
                                               final String entitySuffix,
                                               final String entityPrefix,
                                               final String methodPrefix) {
        return new BaseJavaPoetNamingFactory(packageReference, entitySuffix, entityPrefix, methodPrefix);
    }

    private final PackageReference packageReference;
    private final String entitySuffix;
    private final String entityPrefix;
    private final String methodPrefix;

    private BaseJavaPoetNamingFactory(final PackageReference packageReference,
                                      final String entitySuffix,
                                      final String entityPrefix,
                                      final String methodPrefix) {
        this.packageReference = packageReference;
        this.entitySuffix = entitySuffix;
        this.entityPrefix = entityPrefix;
        this.methodPrefix = methodPrefix;
    }

    @Override
    public PackageReference packageName(final ExtendedTypeElement element) {
        return element.packageInfo().resolve(this.packageReference);
    }

    @Override
    public String type(final ExtendedTypeElement element) {
        final String prefix = element.isEntity() ? this.entityPrefix : EMPTY;
        return prefix + element.getSimpleName() + this.entitySuffix;
    }

    @Override
    public String method(final String propertyName, final String prefix, final String suffix) {
        final String fullPrefix = this.methodPrefix.isEmpty() ? prefix
                : (this.methodPrefix + capitalize(prefix));
        if (fullPrefix.isEmpty()) {
            return propertyName + capitalize(suffix);
        }
        return fullPrefix + capitalize(propertyName) + capitalize(suffix);
    }

    @Override
    public String toString() {
        return "BaseJavaPoetNamingFactory{" +
                "packageReference=" + this.packageReference +
                ", entitySuffix='" + this.entitySuffix + '\'' +
                ", entityPrefix='" + this.entityPrefix + '\'' +
                ", methodPrefix='" + this.methodPrefix + '\'' +
                "}";
    }
}
