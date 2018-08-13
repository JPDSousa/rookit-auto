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
import com.google.common.base.MoreObjects;

final class ImmutableParentPackageReference extends ImmutablePackageReference {

    private final PackageReference parent;
    private final Joiner joiner;

    ImmutableParentPackageReference(final String name, final PackageReference parent, final Joiner joiner) {
        super(name);
        this.parent = parent;
        this.joiner = joiner;
    }

    @Override
    public String fullName() {
        return this.joiner.join(this.parent.fullName(), name());
    }

    @Override
    public PackageReference concat(final String name) {
        return new ImmutableParentPackageReference(name, this, this.joiner);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("parent", this.parent)
                .add("joiner", this.joiner)
                .toString();
    }
}
