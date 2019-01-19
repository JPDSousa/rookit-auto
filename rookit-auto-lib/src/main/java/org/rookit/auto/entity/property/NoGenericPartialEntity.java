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

import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.source.CodeSourceContainer;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

final class NoGenericPartialEntity implements PartialEntity {

    private final OptionalFactory optionalFactory;
    private final CodeSourceContainer<PartialEntity> parents;

    NoGenericPartialEntity(final OptionalFactory optionalFactory, final CodeSourceContainer<PartialEntity> parents) {
        this.optionalFactory = optionalFactory;
        this.parents = parents;
    }

    @Override
    public Optional<Identifier> genericIdentifier() {
        return this.optionalFactory.empty();
    }

    @Override
    public Collection<PartialEntity> parents() {
        return this.parents.asCollection();
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) throws IOException {
        return this.parents.writeTo(filer);
    }

    @Override
    public String toString() {
        return "NoGenericPartialEntity{" +
                "optionalFactory=" + this.optionalFactory +
                ", parents=" + this.parents +
                "}";
    }
}
