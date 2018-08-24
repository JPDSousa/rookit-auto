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
package org.rookit.auto.entity.noop;

import com.google.common.base.MoreObjects;
import org.rookit.auto.entity.AbstractEntity;
import org.rookit.auto.entity.Identifier;
import org.rookit.auto.entity.PartialEntity;
import org.rookit.utils.VoidUtils;

import javax.annotation.processing.Filer;
import java.util.concurrent.CompletableFuture;

final class ExclusionEntity extends AbstractEntity {

    private final Identifier identifier;

    ExclusionEntity(final Identifier identifier, final PartialEntity genericReference) {
        super(genericReference);
        this.identifier = identifier;
    }

    @Override
    public Identifier identifier() {
        return this.identifier;
    }

    @Override
    public CompletableFuture<Void> writeEntityTo(final Filer filer) {
        // Do nothing, as this entity is already implemented
        return CompletableFuture.completedFuture(VoidUtils.returnVoid());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("identifier", this.identifier)
                .toString();
    }
}
