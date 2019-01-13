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

import com.google.common.io.Closer;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.auto.config.ConfigurationModule;
import org.rookit.auto.guice.Self;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.SelfJavaPoetNamingFactory;
import org.rookit.auto.javapoet.type.BaseTypeSourceAdapter;
import org.rookit.auto.javapoet.type.TypeSourceAdapter;
import org.rookit.auto.javax.BaseJavaxRepetitionFactory;
import org.rookit.auto.javax.BaseRepetitiveTypeMirrorFactory;
import org.rookit.auto.javax.JavaxRepetitionFactory;
import org.rookit.auto.javax.JavaxUtilsModule;
import org.rookit.auto.javax.RepetitiveTypeMirrorFactory;
import org.rookit.auto.javax.element.BaseExtendedTypeElementFactory;
import org.rookit.auto.javax.element.ElementModule;
import org.rookit.auto.javax.element.ExtendedTypeElementFactory;
import org.rookit.auto.javax.property.BaseExtendedPropertyFactory;
import org.rookit.auto.javax.property.BasePropertyExtractor;
import org.rookit.auto.javax.property.ExtendedPropertyFactory;
import org.rookit.auto.javax.property.PropertyAdapter;
import org.rookit.auto.javax.property.PropertyExtractor;
import org.rookit.auto.naming.BasePackageReferenceFactory;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.auto.property.BasePropertyAdapter;
import org.rookit.io.path.PathModule;
import org.rookit.utils.guice.Dummy;
import org.rookit.utils.io.DummyInputStream;
import org.rookit.utils.io.DummyOutputStream;
import org.rookit.utils.io.DummyReader;
import org.rookit.utils.io.DummyWriter;
import org.rookit.utils.optional.OptionalFactory;
import org.rookit.utils.optional.OptionalFactoryImpl;
import org.rookit.utils.primitive.VoidUtils;
import org.rookit.utils.primitive.VoidUtilsImpl;
import org.rookit.utils.string.StringUtils;
import org.rookit.utils.string.StringUtilsImpl;
import org.rookit.utils.type.BaseExtendedClassFactory;
import org.rookit.utils.type.ExtendedClassFactory;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

@SuppressWarnings({"MethodMayBeStatic", "FeatureEnvy"})
public final class SourceUtilsModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(new SourceUtilsModule(),
            ConfigurationModule.getModule(),
            PathModule.getModule(),
            ElementModule.getModule(),
            JavaxUtilsModule.getModule());

    public static Module getModule() {
        return MODULE;
    }

    private SourceUtilsModule() {}

    @Override
    protected void configure() {
        bind(Filer.class).to(IdempotentFiler.class).in(Singleton.class);
        bind(PackageReferenceFactory.class).to(BasePackageReferenceFactory.class).in(Singleton.class);
        bind(JavaFileObject.class).annotatedWith(Dummy.class).to(DummyJavaFileObject.class).in(Singleton.class);
        bind(InputStream.class).annotatedWith(Dummy.class).toInstance(DummyInputStream.get());
        bind(OutputStream.class).annotatedWith(Dummy.class).toInstance(DummyOutputStream.get());
        bind(Reader.class).annotatedWith(Dummy.class).toInstance(DummyReader.get());
        bind(Writer.class).annotatedWith(Dummy.class).toInstance(DummyWriter.get());

        bind(ExtendedTypeElementFactory.class).to(BaseExtendedTypeElementFactory.class).in(Singleton.class);
        bind(ExtendedPropertyFactory.class).to(BaseExtendedPropertyFactory.class).in(Singleton.class);
        bind(PropertyAdapter.class).to(BasePropertyAdapter.class).in(Singleton.class);
        bind(EntityHandler.class).to(StatelessEntityHandler.class).in(Singleton.class);
        bind(JavaxRepetitionFactory.class).to(BaseJavaxRepetitionFactory.class).in(Singleton.class);
        bind(RepetitiveTypeMirrorFactory.class).to(BaseRepetitiveTypeMirrorFactory.class).in(Singleton.class);
        bind(PropertyExtractor.class).to(BasePropertyExtractor.class).in(Singleton.class);
        bind(TypeSourceAdapter.class).to(BaseTypeSourceAdapter.class).in(Singleton.class);

        // TODO this should not be here, but Guice is not able to find my UtilsModule :(
        bind(VoidUtils.class).to(VoidUtilsImpl.class).in(Singleton.class);
        bind(StringUtils.class).to(StringUtilsImpl.class).in(Singleton.class);
        bind(OptionalFactory.class).to(OptionalFactoryImpl.class).in(Singleton.class);
        bind(Closer.class).toInstance(Closer.create());
        bind(ExtendedClassFactory.class).to(BaseExtendedClassFactory.class).in(Singleton.class);
        // TODO end todo
    }

    @Singleton
    @Provides
    @Self
    JavaPoetNamingFactory selfQueryNamingFactory(final PackageReferenceFactory packageFactory) {
        return SelfJavaPoetNamingFactory.create(packageFactory);
    }

}
