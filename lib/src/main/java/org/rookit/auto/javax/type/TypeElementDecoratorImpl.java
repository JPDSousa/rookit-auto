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
package org.rookit.auto.javax.type;

import org.rookit.auto.javax.property.ExtendedPropertyExtractor;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

final class TypeElementDecoratorImpl extends AbstractTypeElementDecorator {

    TypeElementDecoratorImpl(final TypeElement delegate,
                             final ElementUtils utils,
                             final OptionalFactory optionalFactory,
                             final PackageReferenceFactory packageFactory,
                             final Collection<Property> properties,
                             final ExtendedPropertyExtractor extractor,
                             final ExtendedTypeMirrorFactory factory) {
        super(delegate, utils, optionalFactory, packageFactory, properties, extractor, factory);
    }

    @Override
    public Optional<ExtendedTypeElement> child() {
        return optionalFactory().empty();
    }

}
