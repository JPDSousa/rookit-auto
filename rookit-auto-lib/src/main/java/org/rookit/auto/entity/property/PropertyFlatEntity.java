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
package org.rookit.auto.entity.property;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.source.TypeSource;
import org.rookit.utils.optional.Optional;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

final class PropertyFlatEntity implements Entity {

    private final Identifier identifier;
    private final TypeSource source;
    private final Collection<Entity> childEntities;
    private final PartialEntity partialEntity;

    PropertyFlatEntity(final Identifier identifier,
                       final Collection<Entity> childEntities,
                       final TypeSource source,
                       final PartialEntity partialEntity) {
        this.identifier = identifier;
        this.source = source;
        this.childEntities = ImmutableSet.copyOf(childEntities);
        this.partialEntity = partialEntity;
    }

    @Override
    public Identifier identifier() {
        return this.identifier;
    }

    @Override
    public Optional<Identifier> genericIdentifier() {
        return this.partialEntity.genericIdentifier();
    }

    @Override
    public Collection<PartialEntity> parents() {
        return this.partialEntity.parents();
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) throws IOException {
        return CompletableFuture.allOf(
                this.partialEntity.writeTo(filer),
                this.source.writeTo(filer),
                writeChildEntities(filer)
        );
    }

    private CompletableFuture<Void> writeChildEntities(final Filer filer) throws IOException {
        final List<CompletableFuture<Void>> ops = Lists.newArrayListWithCapacity(this.childEntities.size());
        for (final Entity entity : this.childEntities) {
            ops.add(entity.writeTo(filer));
        }

        //noinspection ZeroLengthArrayAllocation
        return CompletableFuture.allOf(ops.toArray(new CompletableFuture[0]));
    }

    @Override
    public String toString() {
        return "PropertyFlatEntity{" +
                "identifier=" + this.identifier +
                ", source=" + this.source +
                ", childEntities=" + this.childEntities +
                ", partialEntity=" + this.partialEntity +
                "}";
    }
}
