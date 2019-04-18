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
package org.rookit.auto.entity;

import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.source.CodeSourceContainer;
import org.rookit.utils.optional.OptionalFactory;

import javax.annotation.processing.Filer;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractPartialEntity implements PartialEntity {

    private final Identifier genericIdentifier;
    private final CodeSourceContainer<PartialEntity> parents;
    private final OptionalFactory optionalFactory;

    protected AbstractPartialEntity(final Identifier genericIdentifier,
                                    final CodeSourceContainer<PartialEntity> parents,
                                    final OptionalFactory optionalFactory) {
        this.genericIdentifier = genericIdentifier;
        this.parents = parents;
        this.optionalFactory = optionalFactory;
    }

    @Override
    public org.rookit.utils.optional.Optional<Identifier> genericIdentifier() {
        return this.optionalFactory.of(this.genericIdentifier);
    }

    @Override
    public Collection<PartialEntity> parents() {
        return this.parents.asCollection();
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) {
        return CompletableFuture.allOf(this.parents.writeTo(filer), writePartialEntityTo(filer));
    }

    protected abstract CompletableFuture<Void> writePartialEntityTo(Filer filer);

    @Override
    public String toString() {
        return "AbstractPartialEntity{" +
                "genericIdentifier=" + this.genericIdentifier +
                ", parents=" + this.parents +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
