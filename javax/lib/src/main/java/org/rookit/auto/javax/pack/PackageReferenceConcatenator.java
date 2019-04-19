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
import com.google.common.collect.ImmutableList;
import org.rookit.utils.optional.OptionalFactory;

import java.util.List;
import java.util.regex.Pattern;

final class PackageReferenceConcatenator extends AbstractPackageReference {

    @SuppressWarnings("FieldNotUsedInToString") // not useful
    private final Joiner joiner;
    private final Pattern splitter;
    private final PackageReference current;
    private final PackageReference parent;
    @SuppressWarnings("FieldNotUsedInToString")
    private final List<String> fullName;
    private final int length;
    private final OptionalFactory optionalFactory;

    PackageReferenceConcatenator(final Joiner joiner,
                                 final Pattern splitter,
                                 final PackageReference current,
                                 final PackageReference parent,
                                 final OptionalFactory optionalFactory) {
        this.joiner = joiner;
        this.splitter = splitter;
        this.current = current;
        this.parent = parent;
        this.optionalFactory = optionalFactory;
        this.fullName = ImmutableList.<String>builder()
                .addAll(this.parent.fullNameAsList())
                .addAll(this.current.fullNameAsList())
                .build();
        this.length = this.parent.length() + this.current.length();
    }

    @Override
    public String name() {
        return this.current.name();
    }

    @Override
    public String fullName() {
        return this.joiner.join(this.fullName);
    }

    @Override
    public boolean isSubPackageOf(final PackageReference packageReference) {
        // TODO improve this poor implementation
        return packageReference.fullName().startsWith(this.fullName());
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
        final int parentLength = this.parent.length();
        if (index < parentLength) {
            return this.parent.nameAtIndex(index);
        }
        return this.current.nameAtIndex(index - parentLength);
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
        return "PackageReferenceConcatenator{" +
                ", splitter=" + this.splitter +
                ", current=" + this.current +
                ", parent=" + this.parent +
                ", length=" + this.length +
                "} ";
    }
}
