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

import java.util.regex.Pattern;

class ImmutablePackageReference extends AbstractPackageReference {

    private final String name;

    @SuppressWarnings("FieldNotUsedInToString") // not useful
    private final Joiner joiner;
    private final Pattern splitter;

    ImmutablePackageReference(final String name, final Joiner joiner, final Pattern splitter) {
        this.name = name;
        this.joiner = joiner;
        this.splitter = splitter;
    }

    Joiner joiner() {
        return this.joiner;
    }

    @Override
    Pattern splitter() {
        return this.splitter;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String fullName() {
        return name();
    }

    @Override
    public String toString() {
        return "ImmutablePackageReference{" +
                "name='" + this.name + '\'' +
                ", splitter=" + this.splitter +
                "} " + super.toString();
    }
}
