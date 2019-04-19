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
package org.rookit.auto.javax.property;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.rookit.auto.javax.repetition.JavaxRepetition;
import org.rookit.auto.javax.repetition.JavaxRepetitionFactory;
import org.rookit.auto.javax.type.ElementUtils;
import org.rookit.auto.javax.type.ExtendedTypeElement;
import org.rookit.auto.javax.type.ExtendedTypeElementFactory;
import org.rookit.auto.javax.type.ExtendedTypeMirror;
import org.rookit.convention.annotation.PropertyContainer;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.repetition.Repetition;

import java.util.Objects;

public final class BasePropertyFactory implements PropertyFactory {

    public static PropertyFactory create(final JavaxRepetitionFactory repetitionFactory,
                                         final ElementUtils utils,
                                         final Provider<ExtendedTypeElementFactory> elementFactory) {
        return new BasePropertyFactory(repetitionFactory, utils, elementFactory);
    }

    private final JavaxRepetitionFactory repetitionFactory;
    private final ElementUtils utils;
    private final Provider<ExtendedTypeElementFactory> elementFactory;

    @Inject
    private BasePropertyFactory(final JavaxRepetitionFactory repetitionFactory,
                                final ElementUtils utils,
                                final Provider<ExtendedTypeElementFactory> elementFactory) {
        this.repetitionFactory = repetitionFactory;
        this.utils = utils;
        this.elementFactory = elementFactory;
    }

    @Override
    public Property changeType(final Property source, final ExtendedTypeMirror newReturnType) {
        final Repetition repetition = this.repetitionFactory.fromTypeMirror(newReturnType);
        return new ReturnProperty(source, repetition, newReturnType
        );
    }

    @Override
    public Property changeName(final Property source, final String newName) {
        return new NameProperty(newName, source);
    }

    @Override
    public ContainerProperty changeName(final ContainerProperty source, final String newName) {
        return new NameContainerProperty(newName, source);
    }

    @Override
    public Optional<ContainerProperty> toContainer(final ExtendedProperty property) {
        return property.typeAsElement()
                .filter(ExtendedTypeElement::isPropertyContainer)
                .map(type -> new BaseContainerProperty(property, type));
    }

    @Override
    public ExtendedProperty extend(final Property property) {
        if (property instanceof ExtendedProperty) {
            return (ExtendedProperty) property;
        }

        return new BaseExtendedProperty(property, this.utils, this.elementFactory.get());
    }

    @Override
    public Property create(final String name, final ExtendedTypeMirror type) {
        return create(name, type, this.repetitionFactory.fromTypeMirror(type));
    }

    @Override
    public Property create(final String name,
                           final ExtendedTypeMirror typeMirror,
                           final JavaxRepetition repetition) {
        return create(name, typeMirror, repetition, false);
    }

    @Override
    public Property createFinal(final String name, final ExtendedTypeMirror type) {
        return createFinal(name, type, this.repetitionFactory.fromTypeMirror(type));
    }

    @Override
    public Property createFinal(final String name,
                                final ExtendedTypeMirror type,
                                final JavaxRepetition repetition) {
        return create(name, type, repetition, true);
    }

    private Property create(final String name,
                                    final ExtendedTypeMirror typeMirror,
                                    final JavaxRepetition repetition,
                                    final boolean isFinal) {
        final boolean isContainer = typeMirror.toElement()
                .filter(type -> Objects.nonNull(type.getAnnotation(PropertyContainer.class)))
                .isPresent();
        return new BaseProperty(name, typeMirror, isContainer, isFinal, repetition);
    }

    @Override
    public String toString() {
        return "BasePropertyFactory{" +
                "repetitionFactory=" + this.repetitionFactory +
                ", utils=" + this.utils +
                ", elementFactory=" + this.elementFactory +
                "}";
    }
}
