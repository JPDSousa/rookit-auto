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
import com.google.common.collect.ImmutableList;
import org.rookit.utils.optional.OptionalFactory;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;

final class ImmutableParentPackageReference extends AbstractPackageReference {

    private final PackageReference parent;
    private final int length;
    private final String name;
    @SuppressWarnings("FieldNotUsedInToString")
    private final Joiner joiner;
    private final Pattern splitter;
    @SuppressWarnings("FieldNotUsedInToString")
    private final List<String> fullName;
    private final OptionalFactory optionalFactory;

    ImmutableParentPackageReference(final String name,
                                    final PackageReference parent,
                                    final Joiner joiner,
                                    final Pattern splitter,
                                    final OptionalFactory optionalFactory) {
        this.name = name;
        this.joiner = joiner;
        this.splitter = splitter;
        this.parent = parent;
        this.length = parent.length() + 1;
        this.fullName = ImmutableList.<String>builder()
                .addAll(this.parent.fullNameAsList())
                .add(name)
                .build();
        this.optionalFactory = optionalFactory;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String fullName() {
        return joiner().join(this.fullName);
    }

    @Override
    public boolean isSubPackageOf(final PackageReference packageReference) {
        if (equals(packageReference)) {
            return false;
        }

        final boolean isParentSubPackage = this.parent.isSubPackageOf(packageReference);
        if (!isParentSubPackage) {
            return false;
        }

        // at this point we know that the packages are not equal, so it is safe to compare only the specific
        // package type.
        return packageReference.nameAtIndex(length() - 1).name().equals(this.name);
    }

    @Override
    public PackageReference root() {
        return this.parent.root();
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public PackageReference nameAtIndex(final int index) {
        // TODO use failsafe validations here.
        if (index < 0) {
            //noinspection AutoBoxing no way around.
            throw new IndexOutOfBoundsException(format("Index is negative: %s", index));
        }
        if (index >= length()) {
            //noinspection AutoBoxing no way around.
            throw new IndexOutOfBoundsException(format("Package has %s and you're trying to access index %d",
                    length(), index));
        }
        if (index == (length() - 1)) {
            return new ImmutablePackageReference(this.name, this.joiner, this.splitter, this.optionalFactory);
        }
        return this.parent.nameAtIndex(index);
    }

    @Override
    public List<String> fullNameAsList() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType already immutable
        return this.fullName;
    }

    @Override
    Joiner joiner() {
        return this.joiner;
    }

    @Override
    Pattern splitter() {
        return this.splitter;
    }

    @Override
    OptionalFactory optionalFactory() {
        return this.optionalFactory;
    }

    @Override
    public String toString() {
        return "ImmutableParentPackageReference{" +
                "parent=" + this.parent +
                ", length=" + this.length +
                ", name='" + this.name + '\'' +
                ", splitter=" + this.splitter +
                "} ";
    }
}
