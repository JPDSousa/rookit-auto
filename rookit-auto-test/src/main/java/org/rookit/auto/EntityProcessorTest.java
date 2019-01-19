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

import com.google.common.collect.ImmutableList;
import io.github.glytching.junit.extension.folder.TemporaryFolder;
import io.github.glytching.junit.extension.folder.TemporaryFolderExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
@ExtendWith(TemporaryFolderExtension.class)
@ExtendWith(MockitoExtension.class)
public interface EntityProcessorTest {

    EntityProcessor processor();

    @Test
    default void testInjector(final TemporaryFolder temporaryFolder,
                              @Mock final ProcessingEnvironment environment,
                              @Mock final Elements elementUtils,
                              @Mock final Types typeUtils,
                              @Mock final Messager messager,
                              @Mock final Filer filer,
                              @Mock final TypeElement typeElement,
                              @Mock final TypeMirror mirror,
                              @Mock final PrimitiveType primitiveType,
                              @Mock final FileObject configObject) throws IOException {
        when(environment.getElementUtils()).thenReturn(elementUtils);
        when(environment.getTypeUtils()).thenReturn(typeUtils);
        when(environment.getMessager()).thenReturn(messager);
        when(environment.getFiler()).thenReturn(filer);
        when(elementUtils.getTypeElement(anyString())).thenReturn(typeElement);
        when(typeUtils.erasure(any())).thenReturn(mirror);
        when(typeUtils.getPrimitiveType(any())).thenReturn(primitiveType);
        when(typeElement.asType()).thenReturn(mirror);
        when(mirror.accept(any(), any())).thenReturn(ImmutableList.of());
        when(primitiveType.accept(any(), any())).thenReturn(ImmutableList.of());
        when(filer.getResource(StandardLocation.CLASS_OUTPUT, "", "generation.json"))
                .thenReturn(configObject);
        when(configObject.toUri()).thenReturn(createConfigFile(temporaryFolder));

        final EntityProcessor processor = processor();
        processor.init(environment);
        assertThatCode(() -> processor.injector().getInstance(EntityHandler.class))
                .as("Initializing the Entity Handler")
                .doesNotThrowAnyException();
    }

    default URI createConfigFile(final TemporaryFolder temporaryFolder) throws IOException {
        final Path tempPath = temporaryFolder.createFile("configFile.json").toPath();
        Files.write(tempPath, jsonConfig().getBytes(Charset.defaultCharset()));

        return tempPath.toUri();
    }

    String jsonConfig();
}
