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
package org.rookit.auto.javax.runtime.variable;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.rookit.auto.javax.runtime.NameFactory;
import org.rookit.auto.javax.runtime.RuntimeElementFactory;
import org.rookit.auto.javax.runtime.RuntimeModifierFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeMirrorFactory;
import org.rookit.auto.javax.runtime.RuntimeVariableElementFactory;
import org.rookit.auto.javax.runtime.annotation.RuntimeAnnotatedConstructFactory;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;

final class RuntimeVariableElementFactoryImpl implements RuntimeVariableElementFactory {

    private final RuntimeElementFactory elementFactory;
    private final RuntimeTypeMirrorFactory mirrorFactory;
    private final RuntimeAnnotatedConstructFactory annotatedFactory;
    private final RuntimeModifierFactory modifierFactory;
    private final NameFactory nameFactory;

    @Inject
    private RuntimeVariableElementFactoryImpl(
            final RuntimeElementFactory elementFactory,
            final RuntimeTypeMirrorFactory mirrorFactory,
            final RuntimeAnnotatedConstructFactory annotatedFactory,
            final RuntimeModifierFactory modifierFactory,
            final NameFactory nameFactory) {
        this.elementFactory = elementFactory;
        this.mirrorFactory = mirrorFactory;
        this.annotatedFactory = annotatedFactory;
        this.modifierFactory = modifierFactory;
        this.nameFactory = nameFactory;
    }

    @Override
    public VariableElement createEnum(final Enum<?> enumValue) {
        final Class<?> enumClass = enumValue.getDeclaringClass();
        final Element enumElement = this.elementFactory.createFromClass(enumClass);

        return new RuntimeEnumVariableElement(enumElement, enumValue);
    }

    @Override
    public VariableElement createFromField(final Field field, final Element enclosingElement) {
        return new RuntimeFieldVariableElement(
                this.annotatedFactory.createFromAnnotatedElement(field),
                enclosingElement,
                this.modifierFactory.create(field.getModifiers()),
                this.nameFactory.create(field.getName()),
                this.mirrorFactory.createFromClass(field.getDeclaringClass())
        );
    }

    @Override
    public Collection<VariableElement> createFromExecutable(final Executable executable,
                                                            final Element enclosingElement) {
        return Arrays.stream(executable.getParameters())
                .map(parameter -> createFromParameter(parameter, enclosingElement))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public VariableElement createFromParameter(
            final Parameter parameter,
            final Element enclosingElement) {
        return new RuntimeParameterVariableElement(
                this.annotatedFactory.createFromAnnotatedElement(parameter),
                this.mirrorFactory.createFromClass(parameter.getType()),
                this.modifierFactory.create(parameter.getModifiers()),
                this.nameFactory.create(parameter.getName()),
                enclosingElement
        );
    }

    @Override
    public String toString() {
        return "RuntimeVariableElementFactoryImpl{" +
                "elementFactory=" + this.elementFactory +
                ", mirrorFactory=" + this.mirrorFactory +
                ", annotatedFactory=" + this.annotatedFactory +
                ", modifierFactory=" + this.modifierFactory +
                ", nameFactory=" + this.nameFactory +
                "}";
    }

}
