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

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.rookit.auto.guice.LaConvention;
import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

final class ElementUtilsImpl implements ElementUtils {

    private final Elements elements;
    private final Types types;
    private final TypeParameterExtractor extractor;
    private final Collection<Class<? extends Annotation>> annotations;
    private final OptionalFactory optionalFactory;

    @SuppressWarnings("TypeMayBeWeakened") // due to multibinder
    @Inject
    private ElementUtilsImpl(final Elements elements,
                             final Types types,
                             final TypeParameterExtractor extractor,
                             @LaConvention final Set<Class<? extends Annotation>> annotations,
                             final OptionalFactory optionalFactory) {
        this.elements = elements;
        this.types = types;
        this.extractor = extractor;
        this.annotations = ImmutableSet.copyOf(annotations);
        this.optionalFactory = optionalFactory;
    }

    @Override
    public Optional<Element> toElement(final TypeMirror typeMirror) {
        return this.optionalFactory.ofNullable(this.types.asElement(typeMirror));
    }

    @Override
    public boolean isConventionElement(final AnnotatedConstruct element) {
        for (final Class<? extends Annotation> annotation : this.annotations) {
            if (Objects.nonNull(element.getAnnotation(annotation))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "ElementUtilsImpl{" +
                "elements=" + this.elements +
                ", types=" + this.types +
                ", extractor=" + this.extractor +
                ", annotations=" + this.annotations +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
