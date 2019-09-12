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
package org.rookit.auto.javax.runtime.element;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.rookit.auto.javax.runtime.NameFactory;
import org.rookit.auto.javax.runtime.RuntimeElementFactory;
import org.rookit.auto.javax.runtime.RuntimeElementKindFactory;
import org.rookit.auto.javax.runtime.RuntimeExecutableElementFactory;
import org.rookit.auto.javax.runtime.RuntimeModifierFactory;
import org.rookit.auto.javax.runtime.RuntimePackageElementFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeElementFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeMirrorFactory;
import org.rookit.auto.javax.runtime.RuntimeVariableElementFactory;
import org.rookit.auto.javax.runtime.annotation.RuntimeAnnotatedConstructFactory;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

final class RuntimeElementFactoryImpl implements RuntimeElementFactory {

    private final RuntimeTypeElementFactory typeElementFactory;
    private final RuntimeTypeMirrorFactory typeMirrorFactory;
    private final RuntimeModifierFactory modifierFactory;
    private final NameFactory nameFactory;
    private final RuntimeElementKindFactory kindFactory;
    private final RuntimeExecutableElementFactory executableFactory;
    private final OptionalFactory optionalFactory;
    private final RuntimePackageElementFactory packageFactory;
    private final RuntimeVariableElementFactory variableFactory;
    private final RuntimeAnnotatedConstructFactory annotatedConstructFactory;

    @Inject
    private RuntimeElementFactoryImpl(
            final RuntimeTypeElementFactory typeElementFactory,
            final RuntimeTypeMirrorFactory typeMirrorFactory,
            final RuntimeModifierFactory modifierFactory,
            final NameFactory nameFactory,
            final RuntimeElementKindFactory kindFactory,
            final RuntimeExecutableElementFactory executableFactory,
            final OptionalFactory optionalFactory,
            final RuntimePackageElementFactory packageFactory,
            final RuntimeVariableElementFactory variableFactory,
            final RuntimeAnnotatedConstructFactory annotationFactory) {
        this.typeElementFactory = typeElementFactory;
        this.typeMirrorFactory = typeMirrorFactory;
        this.modifierFactory = modifierFactory;
        this.nameFactory = nameFactory;
        this.kindFactory = kindFactory;
        this.executableFactory = executableFactory;
        this.optionalFactory = optionalFactory;
        this.packageFactory = packageFactory;
        this.variableFactory = variableFactory;
        this.annotatedConstructFactory = annotationFactory;
    }

    @Override
    public Element createFromSupplier(final Supplier<Element> supplier) {
        return new LazyElement(supplier);
    }

    @Override
    public Element createFromClass(final Class<?> clazz) {
        final Name simpleName = this.nameFactory.create(clazz.getSimpleName());
        final TypeMirror typeMirror = this.typeMirrorFactory.createFromClass(clazz);
        final Set<Modifier> modifiers = this.modifierFactory.create(clazz.getModifiers());
        final ElementKind elementKind = this.kindFactory.createFromClass(clazz);
        final Element enclosingElement = createEnclosingElement(clazz);
        final List<? extends Element> enclosedElements = createEnclosedElements(clazz);
        final AnnotatedConstruct annotation = this.annotatedConstructFactory.createFromAnnotatedElement(clazz);

        return new RuntimeElement(
                simpleName,
                typeMirror,
                elementKind,
                modifiers,
                enclosingElement,
                enclosedElements,
                annotation
        );
    }

    @Override
    public Element createFromExecutable(final Executable executable) {
        final Name simpleName = this.nameFactory.create(executable.getName());
        final ExecutableType typeMirror = this.typeMirrorFactory.createExecutableTypeFromExecutable(executable);
        final Set<Modifier> modifiers = this.modifierFactory.create(executable.getModifiers());
        final ElementKind elementKind = this.kindFactory.createFromExecutable(executable);
        final Element enclosingElement = createFromClass(executable.getDeclaringClass());
        final AnnotatedConstruct annotation = this.annotatedConstructFactory.createFromAnnotatedElement(executable);

        return new RuntimeElement(
                simpleName,
                typeMirror,
                elementKind,
                modifiers,
                enclosingElement,
                ImmutableList.of(),
                annotation
        );
    }

    private List<? extends Element> createEnclosedElements(final Class<?> clazz) {
        return ImmutableList.<Element>builder()
                .addAll(createEnclosingClasses(clazz))
                .addAll(createEnclosedConstructors(clazz))
                .addAll(createEnclosedFields(clazz))
                .addAll(createEnclosedMethods(clazz))
                .build();
    }

    private List<? extends Element> createEnclosedMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .map(this.executableFactory::createFromExecutable)
                .collect(ImmutableList.toImmutableList());
    }

    private List<? extends Element> createEnclosedFields(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(field -> this.variableFactory.createFromField(field, ))
                .collect(ImmutableList.toImmutableList());
    }

    private List<? extends Element> createEnclosedConstructors(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .map(this.executableFactory::createFromExecutable)
                .collect(ImmutableList.toImmutableList());
    }

    private List<? extends Element> createEnclosingClasses(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredClasses())
                .map(this.typeElementFactory::createFromClass)
                .collect(ImmutableList.toImmutableList());
    }

    private Optional<Element> createFromEnclosingConstructor(final Class<?> clazz) {
        return this.optionalFactory.ofNullable(clazz.getEnclosingConstructor())
                .map(this.executableFactory::createFromExecutable);
    }

    private Optional<Element> createFromEnclosingMethod(final Class<?> clazz) {
        return this.optionalFactory.ofNullable(clazz.getEnclosingMethod())
                .map(this.executableFactory::createFromExecutable);
    }

    private Optional<Element> createFromEnclosingClass(final Class<?> clazz) {
        // TODO handle security exception
        return this.optionalFactory.ofNullable(clazz.getEnclosingClass())
                .map(this::createFromClass);
    }

    private Element createEnclosingElement(final Class<?> clazz) {
        return createFromEnclosingConstructor(clazz)
                .orElseMaybe(() -> createFromEnclosingMethod(clazz))
                .orElseMaybe(() -> createFromEnclosingClass(clazz))
                .orElseGet(() -> this.packageFactory.createFromPackageElement(clazz.getPackage()));
    }

    @Override
    public String toString() {
        return "RuntimeElementFactoryImpl{" +
                "typeElementFactory=" + this.typeElementFactory +
                ", typeMirrorFactory=" + this.typeMirrorFactory +
                ", modifierFactory=" + this.modifierFactory +
                ", nameFactory=" + this.nameFactory +
                ", kindFactory=" + this.kindFactory +
                ", executableFactory=" + this.executableFactory +
                ", optionalFactory=" + this.optionalFactory +
                ", packageFactory=" + this.packageFactory +
                ", variableFactory=" + this.variableFactory +
                ", annotatedConstructFactory=" + this.annotatedConstructFactory +
                "}";
    }

}
