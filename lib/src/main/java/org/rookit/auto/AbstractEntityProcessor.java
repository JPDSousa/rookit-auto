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
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.rookit.convention.annotation.Entity;
import org.rookit.convention.annotation.EntityExtension;
import org.rookit.utils.guice.Proxied;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractEntityProcessor extends AbstractInjectorEntityProcessor {

    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private Supplier<Injector> injector;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        final Module module = Modules.override(SourceUtilsModule.getModule())
                .with(sourceModule(),
                        new AbstractModule() {
                            @Override
                            protected void configure() {
                                bind(Elements.class).toInstance(processingEnv.getElementUtils());
                                bind(Types.class).toInstance(processingEnv.getTypeUtils());
                                bind(Messager.class).toInstance(processingEnv.getMessager());
                                bind(Filer.class).annotatedWith(Proxied.class).toInstance(processingEnv.getFiler());
                            }
                        });
        this.injector = Suppliers.memoize(() -> Guice.createInjector(ImmutableList.of(module)));
    }

    @Override
    public Injector injector() {
        return this.injector.get();
    }

    protected abstract Module sourceModule();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("injector", this.injector)
                .toString();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(Entity.class.getName(), EntityExtension.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
