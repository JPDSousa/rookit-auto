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

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import org.rookit.auto.config.ConfigurationModule;
import org.rookit.auto.entity.PartialEntityFactory;
import org.rookit.auto.entity.PropertyPartialEntityFactory;
import org.rookit.auto.entity.parent.BaseParentExtractor;
import org.rookit.auto.entity.parent.ParentExtractor;
import org.rookit.auto.entity.property.NoGenericPartialEntityFactory;
import org.rookit.auto.entity.property.NoGenericPropertyPartialEntityFactory;
import org.rookit.auto.guice.NoGeneric;
import org.rookit.auto.guice.Self;
import org.rookit.auto.javapoet.naming.JavaPoetNamingFactory;
import org.rookit.auto.javapoet.naming.SelfJavaPoetNamingFactory;
import org.rookit.auto.javapoet.type.BaseTypeSourceFactory;
import org.rookit.auto.javapoet.type.TypeSourceFactory;
import org.rookit.auto.javax.JavaxUtilsModule;
import org.rookit.auto.javax.property.BaseExtendedPropertyExtractor;
import org.rookit.auto.javax.property.BasePropertyFactory;
import org.rookit.auto.javax.property.ExtendedPropertyExtractor;
import org.rookit.auto.javax.property.PropertyFactory;
import org.rookit.auto.javax.type.BaseExtendedTypeElementFactory;
import org.rookit.auto.javax.type.ExtendedTypeElementFactory;
import org.rookit.auto.javax.type.ExtendedTypeModule;
import org.rookit.auto.naming.NamingModule;
import org.rookit.auto.naming.PackageReferenceFactory;
import org.rookit.auto.source.BaseCodeSourceContainerFactory;
import org.rookit.auto.source.CodeSourceContainerFactory;
import org.rookit.utils.guice.Dummy;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

// TODO break me down into pieces
@SuppressWarnings({"MethodMayBeStatic", "FeatureEnvy"})
public final class SourceUtilsModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(new SourceUtilsModule(),
            ConfigurationModule.getModule(),
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

        bind(ParentExtractor.class).to(BaseParentExtractor.class).in(Singleton.class);
        bind(ExtendedTypeElementFactory.class).to(BaseExtendedTypeElementFactory.class).in(Singleton.class);
        bind(ExtendedPropertyFactory.class).to(BaseExtendedPropertyFactory.class).in(Singleton.class);
        bind(PropertyAdapter.class).to(BasePropertyAdapter.class).in(Singleton.class);
        bind(EntityHandler.class).to(StatelessEntityHandler.class).in(Singleton.class);
        bind(JavaxRepetitionFactory.class).to(BaseJavaxRepetitionFactory.class).in(Singleton.class);
        bind(RepetitiveTypeMirrorFactory.class).to(BaseRepetitiveTypeMirrorFactory.class).in(Singleton.class);
        bind(PropertyExtractor.class).to(BasePropertyExtractor.class).in(Singleton.class);
        bind(TypeSourceAdapter.class).to(BaseTypeSourceAdapter.class).in(Singleton.class);
        bind(PropertyPartialEntityFactory.class).to(NoGenericPropertyPartialEntityFactory.class).in(Singleton.class);
        bind(PartialEntityFactory.class).annotatedWith(NoGeneric.class)
                .to(NoGenericPartialEntityFactory.class).in(Singleton.class);
        bind(CodeSourceContainerFactory.class).to(BaseCodeSourceContainerFactory.class).in(Singleton.class);
    }

    @Singleton
    @Provides
    @Self
    JavaPoetNamingFactory selfQueryNamingFactory(final PackageReferenceFactory packageFactory) {
        return SelfJavaPoetNamingFactory.create(packageFactory);
    }

}