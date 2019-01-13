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

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import java.util.regex.Pattern;

import static java.lang.String.format;

public final class BasePackageReferenceFactory implements PackageReferenceFactory {

    private static final String SEPARATOR = ".";
    private static final Joiner JOINER = Joiner.on(SEPARATOR);

    private static final Pattern SPLITTER = Pattern.compile("\\" + SEPARATOR);

    public static PackageReferenceFactory create(final OptionalFactory optionalFactory) {
        return new BasePackageReferenceFactory(optionalFactory);
    }

    private final OptionalFactory optionalFactory;

    @Inject
    private BasePackageReferenceFactory(final OptionalFactory optionalFactory) {
        this.optionalFactory = optionalFactory;
    }

    @Override
    public PackageReference create(final String fqdn) {
        if (StringUtils.isBlank(fqdn)) {
            throw  new IllegalArgumentException("Cannot create a package with an empty name");
        }
        if (fqdn.startsWith(SEPARATOR)) {
            final String errorMessage = format("Invalid package name '%s', as it starts with a separator", fqdn);
            throw new IllegalArgumentException(errorMessage);
        }

        final String[] packages = SPLITTER.split(fqdn);
        final int length = packages.length;

        // TODO check if not empty
        PackageReference pkg = new ImmutablePackageReference(packages[0], JOINER, SPLITTER);
        for (int i = 1; i < length; i++) {
            pkg = pkg.resolve(packages[i]);
        }
        return pkg;
    }

    @Override
    public Optional<PackageReference> relativize(final PackageReference packageReference,
                                                 final PackageReference basePackage) {
        final String prFullName = packageReference.fullName();
        final String baseName = basePackage.fullName();
        if (!prFullName.startsWith(baseName)) {
            throw new IllegalArgumentException(format("%s cannot be relativized from %s", prFullName, baseName));
        }
        else if (prFullName.equals(baseName)) {
            return this.optionalFactory.empty();
        }

        return this.optionalFactory.of(create(prFullName.substring(baseName.length() + SEPARATOR.length())));
    }

    @Override
    public String toString() {
        return "BasePackageReferenceFactory{" +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
