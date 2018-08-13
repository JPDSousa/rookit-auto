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
package org.rookit.auto.javax.element;

import com.google.common.collect.ImmutableList;
import org.rookit.utils.VoidUtils;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import java.util.Collection;

final class TypeVisitorParameterExtractor implements TypeVisitor<Collection<? extends TypeMirror>, Void>,
        TypeParameterExtractor {

    @Override
    public Collection<TypeMirror> visit(final TypeMirror t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visit(final TypeMirror t) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitPrimitive(final PrimitiveType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitNull(final NullType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitArray(final ArrayType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<? extends TypeMirror> visitDeclared(final DeclaredType t, final Void aVoid) {
        return t.getTypeArguments();
    }

    @Override
    public Collection<TypeMirror> visitError(final ErrorType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitTypeVariable(final TypeVariable t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitWildcard(final WildcardType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitExecutable(final ExecutableType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitNoType(final NoType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitUnknown(final TypeMirror t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitUnion(final UnionType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<TypeMirror> visitIntersection(final IntersectionType t, final Void aVoid) {
        return ImmutableList.of();
    }

    @Override
    public Collection<? extends TypeMirror> extract(final TypeMirror type) {
        return type.accept(this, VoidUtils.returnVoid());
    }
}
