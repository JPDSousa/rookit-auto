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
package org.rookit.auto.javax.pack;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import java.util.regex.Pattern;

abstract class AbstractPackageReference implements PackageReference {

    abstract Joiner joiner();

    abstract Pattern splitter();

    abstract OptionalFactory optionalFactory();

    @Override
    public PackageReference resolve(final String name) {
        final String[] tokens = splitter().split(name);
        final int length = tokens.length;
        PackageReference pkg = new ImmutableParentPackageReference(tokens[0], this, joiner(), splitter(),
                optionalFactory());

        for (int i = 1; i < length; i++) {
            pkg = pkg.resolve(tokens[i]);
        }
        return pkg;
    }

    @Override
    public PackageReference resolve(final PackageReference packageReference) {
        return new PackageReferenceConcatenator(joiner(), splitter(), packageReference, this, optionalFactory());
    }

    @Override
    public Optional<PackageReference> relativize(final PackageReference other) {
        if (this.equals(other)) {
            return this.optionalFactory().empty();
        }

        final PackageReference thisRoot = this.root();
        final PackageReference otherRoot = other.root();
        if (!thisRoot.equals(otherRoot)) {
            return this.optionalFactory().empty();
        }

        final int thisLength = this.length();
        final int thatLength = other.length();
        final int minLength = Math.min(thisLength, thatLength);
        final PackageReference longestPackage = (thisLength > thatLength) ? this : other;

        int cursor = 0;
        while ((cursor < minLength) && this.nameAtIndex(cursor).equals(other.nameAtIndex(cursor))) {
            cursor++;
        }

        return this.optionalFactory().of(subPackage(longestPackage, cursor));
    }

    private PackageReference subPackage(final PackageReference packageReference, final int beginIndex) {
        int cursor = beginIndex;
        PackageReference relativized = packageReference.nameAtIndex(cursor);
        cursor++;

        while(cursor < packageReference.length()) {
            relativized = relativized.resolve(packageReference.nameAtIndex(cursor));
            cursor++;
        }

        return relativized;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if(!(o instanceof PackageReference)) {
            return false;
        }

        return Objects.equal(fullName(), ((PackageReference) o).fullName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fullName());
    }
}
