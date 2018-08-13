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
package org.rookit.auto.javapoet.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import org.rookit.auto.entity.Identifier;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractJavaPoetPartialEntity implements JavaPoetPartialEntity {

    private final JavaPoetIdentifier genericIdentifier;
    private final Collection<PartialEntity> parents;

    protected AbstractJavaPoetPartialEntity(final JavaPoetIdentifier genericIdentifier,
                                            final Collection<? extends PartialEntity> parents) {
        this.genericIdentifier = genericIdentifier;
        this.parents = ImmutableSet.copyOf(parents);
    }

    @Override
    public ClassName genericClassName() {
        return this.genericIdentifier.className();
    }

    @Override
    public Identifier genericIdentifier() {
        return this.genericIdentifier;
    }

    @Override
    public Collection<PartialEntity> parents() {
        return Collections.unmodifiableCollection(this.parents);
    }

    @Override
    public CompletableFuture<Void> writeTo(final Filer filer) throws IOException {
        for (final PartialEntity parent : parents()) {
            parent.writeTo(filer);
        }
        return writePartialEntityTo(filer);
    }

    protected abstract CompletableFuture<Void> writePartialEntityTo(Filer filer) throws IOException;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("genericIdentifier", this.genericIdentifier)
                .add("parents", this.parents)
                .toString();
    }
}
