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

import com.google.inject.Inject;
import org.rookit.auto.config.ProcessorConfig;
import org.rookit.auto.entity.EntityFactory;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;

public final class StatelessEntityHandler extends AbstractEntityHandler {

    public static EntityHandler create(final EntityFactory entityFactory,
                                       final Filer filer,
                                       final ExtendedTypeElementFactory elementFactory,
                                       final ProcessorConfig config,
                                       final Messager messager) {
        return new StatelessEntityHandler(entityFactory, filer, elementFactory, config, messager);
    }

    @SuppressWarnings("TypeMayBeWeakened") // due to guice
    @Inject
    private StatelessEntityHandler(final EntityFactory entityFactory,
                                   final Filer filer,
                                   final ExtendedTypeElementFactory elementFactory,
                                   final ProcessorConfig config,
                                   final Messager messager) {
        super(entityFactory, filer, elementFactory, config, messager);
    }

    @Override
    public void postProcess() {
        // Nothing to do here
    }
}
