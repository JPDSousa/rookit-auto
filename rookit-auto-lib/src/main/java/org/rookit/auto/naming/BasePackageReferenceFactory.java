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

import java.util.regex.Pattern;

public final class BasePackageReferenceFactory implements PackageReferenceFactory {

    private static final Pattern SPLITTER = Pattern.compile("\\.");
    private static final PackageReference ORG = new ImmutablePackageReference("org");
    private static final PackageReference ROOKIT = ORG.concat("rookit");

    public static PackageReferenceFactory create() {
        return new BasePackageReferenceFactory();
    }

    private BasePackageReferenceFactory() {}

    @Override
    public PackageReference basePackage() {
        return ROOKIT;
    }

    @Override
    public PackageReference create(final CharSequence fqdn) {
        final String[] packages = SPLITTER.split(fqdn);
        final int length = packages.length;

        // TODO check if not empty
        PackageReference pkg = new ImmutablePackageReference(packages[0]);
        for (int i = 1; i < length; i++) {
            pkg = pkg.concat(packages[i]);
        }
        return pkg;
    }
}
