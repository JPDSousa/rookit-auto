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

import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.naming.NamingFactory;
import org.rookit.auto.naming.PackageReference;

import static org.apache.commons.text.WordUtils.capitalize;

public final class BasePropertyIdentifierFactory implements PropertyIdentifierFactory {

    public static PropertyIdentifierFactory create(final NamingFactory namingFactory,
                                                   final PackageReference defaultPackage) {
        return new BasePropertyIdentifierFactory(namingFactory, defaultPackage);
    }

    private final NamingFactory namingFactory;
    private final PackageReference defaultPackage;

    private BasePropertyIdentifierFactory(final NamingFactory namingFactory, final PackageReference defaultPackage) {
        this.namingFactory = namingFactory;
        this.defaultPackage = defaultPackage;
    }

    @Override
    public Identifier create(final ExtendedTypeElement element, final ExtendedProperty property) {
        return create(this.namingFactory.packageName(element), property);
    }

    @Override
    public Identifier create(final ExtendedProperty property) {
        return create(this.defaultPackage, property);
    }

    private Identifier create(final PackageReference pack, final ExtendedProperty property) {
        return new EntityIdentifier(
                pack,
                capitalize(this.namingFactory.method(property.name())),
                property.name()
        );
    }

    @Override
    public String toString() {
        return "BasePropertyIdentifierFactory{" +
                "namingFactory=" + this.namingFactory +
                ", defaultPackage=" + this.defaultPackage +
                "}";
    }
}