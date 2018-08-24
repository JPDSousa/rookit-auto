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
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.rookit.utils.convention.annotation.Entity;
import org.rookit.utils.convention.annotation.EntityExtension;
import org.rookit.utils.guice.Proxied;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

public abstract class AbstractEntityProcessor extends AbstractProcessor {

    private Injector injector;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.injector = Guice.createInjector(SourceUtilsModule.getModule(),
                sourceModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Elements.class).toInstance(processingEnv.getElementUtils());
                        bind(Types.class).toInstance(processingEnv.getTypeUtils());
                        bind(Messager.class).toInstance(processingEnv.getMessager());
                        bind(Filer.class).annotatedWith(Proxied.class).toInstance(processingEnv.getFiler());
                    }
                });
    }

    protected abstract Module sourceModule();

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final Messager messager = this.injector.getInstance(Messager.class);
        try {
            final EntityHandler builder = this.injector.getInstance(EntityHandler.class);
            for (final TypeElement annotation : annotations) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Processing: " + annotation.getQualifiedName());
                builder.process(ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(annotation)));
            }
            builder.postProcess();
            return false;
        } catch (final RuntimeException e) {
            final String stackTrace = ExceptionUtils.getStackTrace(e);
            messager.printMessage(Diagnostic.Kind.ERROR, stackTrace);
            throw new RuntimeException(e);
        }
    }

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
