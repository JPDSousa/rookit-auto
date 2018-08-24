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

import org.rookit.auto.entity.AbstractPartialEntity;
import org.rookit.auto.entity.Entity;
import org.rookit.auto.entity.Identifier;
import org.rookit.utils.VoidUtils;

import javax.annotation.processing.Filer;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

final class ExclusionPartialEntity extends AbstractPartialEntity {

    ExclusionPartialEntity(final Identifier genericIdentifier,
                           final Collection<Entity> parents) {
        super(genericIdentifier, parents);
    }

    @Override
    protected CompletableFuture<Void> writePartialEntityTo(final Filer filer) {
        return CompletableFuture.completedFuture(VoidUtils.returnVoid());
    }

}
