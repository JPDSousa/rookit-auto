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
package org.rookit.auto.javapoet.identifier;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.squareup.javapoet.ClassName;
import org.rookit.auto.naming.NamingFactory;

import javax.lang.model.element.TypeElement;
import java.util.Map;

public final class BaseJavaPoetIdentifierFactory implements JavaPoetIdentifierFactory {

    public static JavaPoetIdentifierFactory create(final NamingFactory namingFactory) {
        return new BaseJavaPoetIdentifierFactory(namingFactory);
    }

    private final NamingFactory namingFactory;
    private final Map<String, JavaPoetIdentifier> cache;

    private BaseJavaPoetIdentifierFactory(final NamingFactory namingFactory) {
        this.namingFactory = namingFactory;
        this.cache = Maps.newHashMap();
    }

    @Override
    public JavaPoetIdentifier create(final TypeElement typeElement) {
        return this.cache.computeIfAbsent(typeElement.getQualifiedName().toString(), name -> createNew(typeElement));
    }

    private JavaPoetIdentifier createNew(final TypeElement typeElement) {
        final ClassName className = this.namingFactory.type(typeElement);
        return new JavaPoetIdentifierImpl(className, typeElement.getQualifiedName().toString());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("namingFactory", this.namingFactory)
                .add("cache", this.cache)
                .toString();
    }
}
