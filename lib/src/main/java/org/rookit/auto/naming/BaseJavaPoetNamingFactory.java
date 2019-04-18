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
import org.rookit.utils.string.template.Template1;

public final class BaseJavaPoetNamingFactory  implements JavaPoetNamingFactory {

    public static JavaPoetNamingFactory create(final PackageReference packageReference,
                                               final Template1 typeTemplate,
                                               final Template1 methodTemplate) {
        return new BaseJavaPoetNamingFactory(packageReference, typeTemplate, methodTemplate);
    }

    private final PackageReference packageReference;
    private final Template1 typeTemplate;
    private final Template1 methodTemplate;

    private BaseJavaPoetNamingFactory(final PackageReference packageReference,
                                      final Template1 typeTemplate,
                                      final Template1 methodTemplate) {
        this.packageReference = packageReference;
        this.typeTemplate = typeTemplate;
        this.methodTemplate = methodTemplate;
    }

    @Override
    public PackageReference packageName(final ExtendedTypeElement element) {
        return packageName(element.packageInfo());
    }

    @Override
    public PackageReference packageName(final PackageReference packageReference) {
        return packageReference.resolve(this.packageReference);
    }

    @Override
    public String type(final ExtendedTypeElement element) {
        return this.typeTemplate.build(element.getSimpleName());
    }

    @Override
    public String type(final PackageReference packageReference) {
        return this.typeTemplate.build(packageReference.name());
    }

    @Override
    public String method(final String propertyName) {
        return method(propertyName, this.methodTemplate);
    }

    @Override
    public String method(final String propertyName, final Template1 template) {
        return template.build(propertyName);
    }

    @Override
    public String toString() {
        return "BaseJavaPoetNamingFactory{" +
                "packageReference=" + this.packageReference +
                ", typeTemplate=" + this.typeTemplate +
                ", methodTemplate=" + this.methodTemplate +
                "}";
    }
}
