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
package org.rookit.auto.javax.element;

import com.google.inject.Inject;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.utils.optional.OptionalFactory;

import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.stream.Collectors;

public final class BaseExtendedTypeElementFactory implements ExtendedTypeElementFactory {

    public static ExtendedTypeElementFactory create(final PackageReferenceFactory packageFactory,
                                                    final OptionalFactory optionalFactory,
                                                    final PropertyExtractor extractor,
                                                    final ElementUtils utils) {
        return new BaseExtendedTypeElementFactory(packageFactory, optionalFactory, extractor, utils);
    }

    private final PackageReferenceFactory packageFactory;
    private final OptionalFactory optionalFactory;
    private final PropertyExtractor extractor;
    private final ElementUtils utils;

    @Inject
    private BaseExtendedTypeElementFactory(final PackageReferenceFactory packageFactory,
                                           final OptionalFactory optionalFactory,
                                           final PropertyExtractor extractor,
                                           final ElementUtils utils) {
        this.packageFactory = packageFactory;
        this.optionalFactory = optionalFactory;
        this.extractor = extractor;
        this.utils = utils;
    }

    @Override
    public ExtendedTypeElement extend(final TypeElement baseElement) {
        final Collection<ExtendedProperty> properties = this.extractor.fromType(baseElement)
                .collect(Collectors.toList());
        return new TypeElementDecoratorImpl(baseElement, this.utils,
                this.optionalFactory, this.packageFactory, properties, this.extractor);
    }

    @Override
    public String toString() {
        return "BaseExtendedTypeElementFactory{" +
                "packageFactory=" + this.packageFactory +
                ", optionalFactory=" + this.optionalFactory +
                ", extractor=" + this.extractor +
                ", utils=" + this.utils +
                "}";
    }
}
