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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;
import org.rookit.auto.javax.pack.PackageReference;

final class EntityIdentifier implements JavaPoetIdentifier {

    private final PackageReference packageReference;
    private final String name;

    EntityIdentifier(final PackageReference packageReference, final String name) {
        this.packageReference = packageReference;
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public PackageReference packageName() {
        return this.packageReference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        final EntityIdentifier otherIdentifier = (EntityIdentifier) o;
        return Objects.equal(this.packageReference, otherIdentifier.packageReference) &&
                Objects.equal(this.name, otherIdentifier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.packageReference, this.name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("packageReference", this.packageReference)
                .add("name", this.name)
                .toString();
    }
}
