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
package org.rookit.auto.javapoet.naming;

import com.google.common.base.MoreObjects;
import org.rookit.auto.javax.element.ExtendedTypeElement;
import org.rookit.auto.naming.PackageReference;
import org.rookit.auto.naming.PackageReferenceFactory;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import java.util.Objects;

import static org.apache.commons.text.WordUtils.capitalize;

public final class SelfJavaPoetNamingFactory implements JavaPoetNamingFactory {

    public static JavaPoetNamingFactory create(final PackageReferenceFactory referenceFactory) {
        return new SelfJavaPoetNamingFactory(referenceFactory);
    }

    private final PackageReferenceFactory referenceFactory;

    private SelfJavaPoetNamingFactory(final PackageReferenceFactory referenceFactory) {
        this.referenceFactory = referenceFactory;
    }

    @Override
    public PackageReference packageName(final ExtendedTypeElement element) {
        return packageOf(element);
    }

    private PackageReference packageOf(final Element element) {
        if (element instanceof PackageElement) {
            return this.referenceFactory.create((PackageElement) element);
        }
        final Element enclosing = element.getEnclosingElement();
        if (Objects.isNull(enclosing)) {
            throw new IllegalArgumentException("Cannot extract package from: " + element);
        }
        return packageOf(enclosing);
    }

    @Override
    public String type(final ExtendedTypeElement element) {
        return element.getSimpleName().toString();
    }

    @Override
    public String method(final String propertyName, final String prefix, final String suffix) {
        return capitalize(prefix) + capitalize(propertyName) + capitalize(suffix);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("referenceFactory", this.referenceFactory)
                .toString();
    }
}
