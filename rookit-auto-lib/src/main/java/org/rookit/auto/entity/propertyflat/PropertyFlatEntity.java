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
package org.rookit.auto.entity.propertyflat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.source.TypeSource;
import org.rookit.utils.optional.OptionalFactory;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

final class PropertyFlatEntity implements Entity {

    private final Collection<PartialEntity> parents;
    private final Identifier identifier;
    private final TypeSource source;
    private final Collection<Entity> childEntities;
    private final OptionalFactory optionalFactory;

    PropertyFlatEntity(final Collection<PartialEntity> parents,
                       final Identifier identifier,
                       final Collection<Entity> childEntities,
                       final TypeSource source,
                       final OptionalFactory optionalFactory) {
        this.parents = ImmutableSet.copyOf(parents);
        this.identifier = identifier;
        this.source = source;
        this.childEntities = ImmutableSet.copyOf(childEntities);
        this.optionalFactory = optionalFactory;
    }

    @Override
    public Identifier identifier() {
        return this.identifier;
    }

    @Override
    public org.rookit.utils.optional.Optional<Identifier> genericIdentifier() {
        return this.optionalFactory.empty();
    }

    @Override
    public Collection<PartialEntity> parents() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return this.parents;
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) throws IOException {
        final List<CompletableFuture<Void>> ops = ImmutableList.of(
                this.source.writeTo(filer),
                writeParents(filer),
                writeChildEntities(filer)
        );

        //noinspection ZeroLengthArrayAllocation
        return CompletableFuture.allOf(ops.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<Void> writeChildEntities(final Filer filer) throws IOException {
        final List<CompletableFuture<Void>> ops = Lists.newArrayListWithCapacity(this.childEntities.size());
        for (final Entity entity : this.childEntities) {
            ops.add(entity.writeTo(filer));
        }

        //noinspection ZeroLengthArrayAllocation
        return CompletableFuture.allOf(ops.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<Void> writeParents(final Filer filer) throws IOException {
        final List<CompletableFuture<Void>> ops = Lists.newArrayListWithCapacity(this.parents.size());
        for (final PartialEntity parent : this.parents) {
            ops.add(parent.writeTo(filer));
        }

        //noinspection ZeroLengthArrayAllocation
        return CompletableFuture.allOf(ops.toArray(new CompletableFuture[0]));
    }

    @Override
    public String toString() {
        return "PropertyFlatEntity{" +
                "parents=" + this.parents +
                ", identifier=" + this.identifier +
                ", source=" + this.source +
                ", childEntities=" + this.childEntities +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
