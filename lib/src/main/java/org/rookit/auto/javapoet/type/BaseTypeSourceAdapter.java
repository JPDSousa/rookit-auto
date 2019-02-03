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

import com.google.inject.Inject;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.rookit.auto.identifier.Identifier;
import org.rookit.auto.source.TypeSource;
import org.rookit.utils.primitive.VoidUtils;

import javax.lang.model.element.Modifier;

public final class BaseTypeSourceAdapter implements TypeSourceAdapter {

    public static TypeSourceAdapter create(final VoidUtils voidUtils) {
        return new BaseTypeSourceAdapter(voidUtils);
    }

    private final VoidUtils voidUtils;

    @Inject
    private BaseTypeSourceAdapter(final VoidUtils voidUtils) {
        this.voidUtils = voidUtils;
    }

    @Override
    public TypeSource fromTypeSpec(final Identifier identifier, final TypeSpec source) {
        if ((source.kind != TypeSpec.Kind.INTERFACE) && (source.kind != TypeSpec.Kind.ANNOTATION)) {
            return new BaseTypeSource(identifier, createToString(source), this.voidUtils);
        }
        return new BaseTypeSource(identifier, source, this.voidUtils);
    }

    private TypeSpec createToString(final TypeSpec original) {
        final CodeBlock.Builder codeBlock = CodeBlock.builder()
                .add("return \"$L{\" +\n", original.name);
        for (final FieldSpec field : original.fieldSpecs) {
            codeBlock.add("\"$L=\" + this.$L +\n", field.name, field.name);
        }
        codeBlock.add("\"} \" + super.toString();\n");

        final MethodSpec toString = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addCode(codeBlock.build())
                .build();
        return original.toBuilder()
                .addMethod(toString)
                .build();
    }

    @Override
    public String toString() {
        return "BaseTypeSourceAdapter{" +
                "voidUtils=" + this.voidUtils +
                "}";
    }
}
