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
package org.rookit.auto.javapoet;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.rookit.auto.javapoet.doc.DocModule;
import org.rookit.auto.javapoet.identifier.IdentifierModule;
import org.rookit.auto.javapoet.method.MethodModule;
import org.rookit.auto.javapoet.type.TypeSpecModule;

public final class SourceJavaPoetLibModule extends AbstractModule {

    private static final Module MODULE = Modules.combine(
            DocModule.getModule(),
            IdentifierModule.getModule(),
            MethodModule.getModule(),
            TypeSpecModule.getModule(),
            new SourceJavaPoetLibModule()
    );

    public static Module getModule() {
        return MODULE;
    }

    private SourceJavaPoetLibModule() {}

}
