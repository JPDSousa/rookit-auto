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
package org.rookit.auto.source;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;

import javax.annotation.processing.Filer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

final class BaseCodeSourceContainer<T extends CodeSource> implements CodeSourceContainer<T> {

    private final Collection<T> collection;

    BaseCodeSourceContainer(final Collection<T> collection) {
        this.collection = ImmutableSet.copyOf(collection);
    }

    @Override
    public Collection<T> asCollection() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType already immutable
        return this.collection;
    }

    @Override
    public StreamEx<T> stream() {
        return StreamEx.of(this.collection);
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        final List<CompletableFuture<Void>> ops = Lists.newArrayListWithCapacity(this.collection.size());
        for (final T codeSource : this.collection) {
            ops.add(codeSource.writeTo(filer));
        }

        //noinspection ZeroLengthArrayAllocation
        return CompletableFuture.allOf(ops.toArray(new CompletableFuture[0]));
    }

    @Override
    public String toString() {
        return "BaseCodeSourceContainer{" +
                "collection=" + this.collection +
                "}";
    }
}
