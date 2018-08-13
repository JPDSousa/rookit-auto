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
package org.rookit.auto;

import com.google.common.base.MoreObjects;
import org.rookit.auto.javapoet.entity.JavaPoetEntity;
import org.rookit.auto.javapoet.entity.JavaPoetEntityFactory;
import org.rookit.auto.javax.element.ElementUtils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

public abstract class AbstractEntityHandler implements EntityHandler {

    private final JavaPoetEntityFactory entityFactory;
    private final ElementUtils utils;
    private final Filer filer;

    protected AbstractEntityHandler(final JavaPoetEntityFactory entityFactory,
                                    final ElementUtils utils,
                                    final Filer filer) {
        this.entityFactory = entityFactory;
        this.utils = utils;
        this.filer = filer;
    }

    protected ElementUtils utils() {
        return this.utils;
    }

    @Override
    public void process(final TypeElement element) {
        try {
            final JavaPoetEntity javaPoetEntity = this.entityFactory.create(this.utils.extend(element));
            javaPoetEntity.writeTo(this.filer);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entityFactory", this.entityFactory)
                .add("utils", this.utils)
                .add("filer", this.filer)
                .toString();
    }
}
