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

import com.google.inject.Inject;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.naming.PackageReference;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.utils.guice.Self;
import org.rookit.utils.string.template.Template1;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import java.util.Objects;

import static org.apache.commons.text.WordUtils.capitalize;

public final class SelfJavaPoetNamingFactory implements JavaPoetNamingFactory {

    public static JavaPoetNamingFactory create(final PackageReferenceFactory referenceFactory,
                                               final Template1 noopTemplate) {
        return new SelfJavaPoetNamingFactory(referenceFactory, noopTemplate);
    }

    private final PackageReferenceFactory referenceFactory;
    private final Template1 noopTemplate;

    @Inject
    private SelfJavaPoetNamingFactory(final PackageReferenceFactory referenceFactory,
                                      @Self final Template1 noopTemplate) {
        this.referenceFactory = referenceFactory;
        this.noopTemplate = noopTemplate;
    }

    @Override
    public PackageReference packageName(final ExtendedTypeElement element) {
        return packageOf(element);
    }

    @Override
    public PackageReference packageName(final PackageReference packageReference) {
        return packageReference;
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
    public String type(final PackageReference packageReference) {
        return capitalize(packageReference.name());
    }

    @Override
    public String method(final String propertyName) {
        return method(propertyName, this.noopTemplate);
    }

    @Override
    public String method(final String propertyName, final Template1 template1) {
        return template1.build(propertyName);
    }

    @Override
    public String toString() {
        return "SelfJavaPoetNamingFactory{" +
                "referenceFactory=" + this.referenceFactory +
                ", noopTemplate=" + this.noopTemplate +
                "}";
    }
}
