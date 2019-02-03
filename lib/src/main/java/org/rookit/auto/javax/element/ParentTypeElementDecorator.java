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

import com.google.common.base.MoreObjects;
import org.rookit.auto.javax.ExtendedTypeMirrorFactory;
import org.rookit.auto.javax.property.ExtendedProperty;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

final class ParentTypeElementDecorator extends AbstractTypeElementDecorator {

    private final ExtendedTypeElement child;

    ParentTypeElementDecorator(final TypeElement delegate,
                               final ExtendedTypeElement child,
                               final ElementUtils utils,
                               final OptionalFactory optionalFactory,
                               final PackageReferenceFactory packageFactory,
                               final Collection<ExtendedProperty> properties,
                               final PropertyExtractor extractor,
                               final ExtendedTypeMirrorFactory factory) {
        super(delegate, utils, optionalFactory, packageFactory, properties, extractor, factory);
        this.child = child;
    }

    @Override
    public Optional<ExtendedTypeElement> child() {
        return optionalFactory().of(this.child);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("child", this.child)
                .toString();
    }
}
