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
package org.rookit.auto.javapoet.method;

import com.google.inject.Inject;
import com.squareup.javapoet.MethodSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.JavaPoetFactory;
import org.rookit.auto.javax.property.Property;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.utils.string.template.Template1;

public final class TemplateMethodFactory implements JavaPoetFactory<MethodSpec> {

    public static JavaPoetFactory<MethodSpec> create(final MethodSpecFactory methodSpecFactory,
                                                     final Template1 template) {
        return new TemplateMethodFactory(methodSpecFactory, template);
    }

    private final MethodSpecFactory methodSpecFactory;
    private final Template1 template;

    @Inject
    private TemplateMethodFactory(final MethodSpecFactory methodSpecFactory, final Template1 template) {
        this.methodSpecFactory = methodSpecFactory;
        this.template = template;
    }

    @Override
    public StreamEx<MethodSpec> create(final ExtendedTypeElement owner) {
        return StreamEx.of(owner.properties())
                .map(Property::name)
                .map(name -> this.methodSpecFactory.create(name, this.template));
    }

    @Override
    public StreamEx<MethodSpec> create(final ExtendedTypeElement owner, final Property property) {
        return StreamEx.empty();
    }

    @Override
    public String toString() {
        return "TemplateMethodFactory{" +
                "methodSpecFactory=" + this.methodSpecFactory +
                ", template=" + this.template +
                "}";
    }
}
