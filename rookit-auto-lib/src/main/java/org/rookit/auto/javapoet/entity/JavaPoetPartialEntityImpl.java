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
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.auto.javapoet.identifier.JavaPoetIdentifier;
import org.rookit.utils.VoidUtils;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

final class JavaPoetPartialEntityImpl extends AbstractJavaPoetPartialEntity {

    private final TypeSpec source;

    JavaPoetPartialEntityImpl(final JavaPoetIdentifier genericIdentifier,
                              final Collection<? extends PartialEntity> parents,
                              final TypeSpec source) {
        super(genericIdentifier, parents);
        this.source = source;
    }

    @Override
    protected CompletableFuture<Void> writePartialEntityTo(final Filer filer) throws IOException {
        JavaFile.builder(genericIdentifier().packageName(), this.source)
                .build()
                .writeTo(filer);
        return CompletableFuture.completedFuture(VoidUtils.returnVoid());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("source", this.source)
                .toString();
    }
}
