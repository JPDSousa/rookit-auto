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
package org.rookit.auto.javapoet.type;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import one.util.streamex.StreamEx;
import org.rookit.auto.javapoet.doc.JavaPoetJavadocTemplate1;
import org.rookit.auto.javax.ExtendedElement;
import org.rookit.auto.javax.visitor.ExtendedElementVisitor;
import org.rookit.auto.javax.visitor.GenericStreamExBuilder;
import org.rookit.auto.javax.visitor.StreamExBuilder;

import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

public interface GenericTypeSpecBuilder<B extends GenericTypeSpecBuilder<B, V, P>,
        V extends ExtendedElementVisitor<StreamEx<TypeSpec.Builder>, P>, P>
        extends GenericStreamExBuilder<B, V, TypeSpec.Builder, P> {

    B withAnnotations(Iterable<AnnotationSpec> annotations);

    B copyBodyFrom(TypeMirror typeMirror);

    B copyBodyFrom(Function<ExtendedElement, TypeMirror> extractionFunction);

    B withClassJavadoc(JavaPoetJavadocTemplate1 template);

    <V1 extends ExtendedElementVisitor<StreamEx<TypeSpec>, P>> StreamExBuilder<V1, TypeSpec, P>
    buildTypeSpec(Function<ExtendedElementVisitor<StreamEx<TypeSpec>, P>, V1> downcastAdapter);

    default StreamExBuilder<ExtendedElementVisitor<StreamEx<TypeSpec>, P>, TypeSpec, P>
    buildTypeSpec() {
        return buildTypeSpec(element -> element);
    }

}
