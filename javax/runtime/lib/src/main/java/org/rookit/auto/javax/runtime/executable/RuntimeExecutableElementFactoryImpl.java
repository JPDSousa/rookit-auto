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
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.rookit.auto.javax.runtime.RuntimeElementFactory;
import org.rookit.auto.javax.runtime.RuntimeExecutableElementFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeMirrorFactory;
import org.rookit.auto.javax.runtime.RuntimeTypeParameterElementFactory;
import org.rookit.auto.javax.runtime.RuntimeVariableElementFactory;
import org.rookit.auto.javax.runtime.annotation.RuntimeAnnotationValueFactory;

import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

final class RuntimeExecutableElementFactoryImpl implements RuntimeExecutableElementFactory {

    private final Map<Executable, ExecutableElement> createdElements;
    private final RuntimeElementFactory elementFactory;
    private final RuntimeAnnotationValueFactory annotationValueFactory;
    private final RuntimeVariableElementFactory variableFactory;
    private final RuntimeTypeParameterElementFactory parameterFactory;
    private final RuntimeTypeMirrorFactory typeMirrorFactory;

    @Inject
    private RuntimeExecutableElementFactoryImpl(
            final RuntimeElementFactory elementFactory,
            final RuntimeAnnotationValueFactory annotationFactory,
            final RuntimeVariableElementFactory variableFactory,
            final RuntimeTypeParameterElementFactory parameterFactory,
            final RuntimeTypeMirrorFactory typeMirrorFactory) {
        this.elementFactory = elementFactory;
        this.annotationValueFactory = annotationFactory;
        this.variableFactory = variableFactory;
        this.parameterFactory = parameterFactory;
        this.typeMirrorFactory = typeMirrorFactory;
        this.createdElements = Maps.newHashMap();
    }

    @Override
    public ExecutableElement createFromExecutable(final Executable executable) {
        return this.createdElements.computeIfAbsent(executable, this::doCreate);
    }

    private ExecutableElement doCreate(final Executable executable) {
        final Element elementPromise = this.elementFactory.createFromSupplier(() -> promiseFor(executable));
        return new RuntimeExecutableElement(
                executable,
                this.parameterFactory.createFrom(elementPromise, executable),
                createReturnType(executable),
                this.variableFactory.createFromExecutable(executable, elementPromise),
                // FIXME what is a receiver type?
                this.typeMirrorFactory.noType(),
                createThrownTypes(executable),
                createDefaultValue(executable),
                this.elementFactory.createFromExecutable(executable));
    }

    // FIXME this is a dangerous strategy, but it's the only way I see so workaround the cyclic dependecy in the API
    private ExecutableElement promiseFor(final Executable executable) {
        final ExecutableElement executableElement = this.createdElements.get(executable);
        if (Objects.isNull(executableElement)) {
            final String errMsg = format("%s for '%s' is not yet created. This strategy is used as a workaround "
                                                 + "for the cyclic dependency that with which the Element API is "
                                                 + "designed (i.e. one can traverse the element graph in both "
                                                 + "enclosed to enclosing and enclosing to enclosed ways). As such, "
                                                 + "'%s::getEnclosing' must not be called before during the creation "
                                                 + "process.", ExecutableElement.class.getName(), executable,
                                         Element.class.getName());
            throw new IllegalStateException(errMsg);
        }
        return executableElement;
    }

    @Nullable
    private AnnotationValue createDefaultValue(final Executable executable) {
        if (executable instanceof Method) {
            return this.annotationValueFactory.createFromObject(((Method) executable).getDefaultValue());
        }
        // to comply with interface contract :(
        return null;
    }

    private Collection<TypeMirror> createThrownTypes(final Executable executable) {
        return Arrays.stream(executable.getExceptionTypes())
                .map(this.typeMirrorFactory::createFromClass)
                .collect(ImmutableList.toImmutableList());
    }

    private TypeMirror createReturnType(final Executable executable) {
        if (executable instanceof Method) {
            return this.typeMirrorFactory.createFromClass(((Method) executable).getReturnType());
        }
        return this.typeMirrorFactory.noType();
    }

    @Override
    public String toString() {
        return "RuntimeExecutableElementFactoryImpl{" +
                "createdElements=" + this.createdElements +
                ", elementFactory=" + this.elementFactory +
                ", annotationValueFactory=" + this.annotationValueFactory +
                ", variableFactory=" + this.variableFactory +
                ", parameterFactory=" + this.parameterFactory +
                ", typeMirrorFactory=" + this.typeMirrorFactory +
                "}";
    }

}
