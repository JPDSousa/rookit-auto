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
package org.rookit.auto.entity.nopartial;

import com.google.common.collect.ImmutableList;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.source.TypeSource;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

final class NoPartialEntity implements Entity {

    private final Identifier identifier;
    private final OptionalFactory optionalFactory;
    private final TypeSource source;

    NoPartialEntity(final Identifier identifier, final OptionalFactory optionalFactory, final TypeSource source) {
        this.identifier = identifier;
        this.optionalFactory = optionalFactory;
        this.source = source;
    }

    @Override
    public Identifier identifier() {
        return this.identifier;
    }

    @Override
    public Optional<Identifier> genericIdentifier() {
        return this.optionalFactory.empty();
    }

    @Override
    public Collection<PartialEntity> parents() {
        return ImmutableList.of();
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) throws IOException {
        return this.source.writeTo(filer);
    }

    @Override
    public String toString() {
        return "NoPartialEntity{" +
                "identifier=" + this.identifier +
                ", optionalFactory=" + this.optionalFactory +
                ", source=" + this.source +
                "}";
    }
}
