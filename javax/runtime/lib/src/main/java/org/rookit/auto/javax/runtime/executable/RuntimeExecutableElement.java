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
package org.rookit.auto.javax.runtime.executable;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class RuntimeExecutableElement implements ExecutableElement {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(RuntimeExecutableElement.class);

    private final Executable executable;
    private final List<? extends TypeParameterElement> typeParameters;
    private final TypeMirror returnType;
    private final List<? extends VariableElement> parameters;
    private final TypeMirror receiverType;
    private final List<? extends TypeMirror> thrownTypes;
    private final AnnotationValue defaultValue;
    private final Element element;

    RuntimeExecutableElement(
            final Executable executable,
            final Collection<? extends TypeParameterElement> typeParameters,
            final TypeMirror returnType,
            final Collection<? extends VariableElement> parameters,
            final TypeMirror receiverType,
            final Collection<? extends TypeMirror> thrownTypes,
            @Nullable final AnnotationValue defaultValue,
            final Element element) {
        this.executable = executable;
        this.typeParameters = ImmutableList.copyOf(typeParameters);
        this.returnType = returnType;
        this.parameters = ImmutableList.copyOf(parameters);
        this.receiverType = receiverType;
        this.thrownTypes = ImmutableList.copyOf(thrownTypes);
        this.defaultValue = defaultValue;
        this.element = element;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return this.typeParameters;
    }

    @Override
    public TypeMirror getReturnType() {
        return this.returnType;
    }

    @Override
    public List<? extends VariableElement> getParameters() {
        return this.parameters;
    }

    @Override
    public TypeMirror getReceiverType() {
        return this.receiverType;
    }

    @Override
    public boolean isVarArgs() {
        logger.trace("Delegating call to '{}'", this.executable);
        return this.executable.isVarArgs();
    }

    @Override
    public boolean isDefault() {
        logger.warn("Reflection does not permit default modifier inspection. Returning false");
        return false;
    }

    @Override
    public List<? extends TypeMirror> getThrownTypes() {
        return this.thrownTypes;
    }

    @Nullable
    @Override
    public AnnotationValue getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public TypeMirror asType() {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.asType();
    }

    @Override
    public ElementKind getKind() {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getKind();
    }

    @Override
    public Set<Modifier> getModifiers() {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getModifiers();
    }

    @Override
    public Name getSimpleName() {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getSimpleName();
    }

    @Override
    public Element getEnclosingElement() {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getEnclosingElement();
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getEnclosedElements();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getAnnotationMirrors();
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        logger.trace("Delegating call to '{}'", this.element.getSimpleName());
        return this.element.getAnnotationsByType(annotationType);
    }

    @Override
    public <R, P> R accept(final ElementVisitor<R, P> v, final P p) {
        logger.trace("Visiting executable");
        return v.visitExecutable(this, p);
    }

    @Override
    public String toString() {
        return "RuntimeExecutableElement{" +
                "executable=" + this.executable +
                ", typeParameters=" + this.typeParameters +
                ", returnType=" + this.returnType +
                ", parameters=" + this.parameters +
                ", receiverType=" + this.receiverType +
                ", thrownTypes=" + this.thrownTypes +
                ", defaultValue=" + this.defaultValue +
                ", element=" + this.element +
                "}";
    }

}
