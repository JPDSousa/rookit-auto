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
package org.rookit.auto;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.Writer;
import java.util.List;
import java.util.Map;

final class RuntimeElements implements Elements {

    @Override
    public PackageElement getPackageElement(final CharSequence name) {
        return null;
    }

    @Override
    public TypeElement getTypeElement(final CharSequence name) {
        return null;
    }

    @Override
    public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(
            final AnnotationMirror a) {
        return null;
    }

    @Override
    public String getDocComment(final Element e) {
        return null;
    }

    @Override
    public boolean isDeprecated(final Element e) {
        return false;
    }

    @Override
    public Name getBinaryName(final TypeElement type) {
        return null;
    }

    @Override
    public PackageElement getPackageOf(final Element type) {
        return null;
    }

    @Override
    public List<? extends Element> getAllMembers(final TypeElement type) {
        return null;
    }

    @Override
    public List<? extends AnnotationMirror> getAllAnnotationMirrors(final Element e) {
        return null;
    }

    @Override
    public boolean hides(final Element hider, final Element hidden) {
        return false;
    }

    @Override
    public boolean overrides(
            final ExecutableElement overrider, final ExecutableElement overridden, final TypeElement type) {
        return false;
    }

    @Override
    public String getConstantExpression(final Object value) {
        return null;
    }

    @Override
    public void printElements(final Writer w, final Element... elements) {

    }

    @Override
    public Name getName(final CharSequence cs) {
        return null;
    }

    @Override
    public boolean isFunctionalInterface(final TypeElement type) {
        return false;
    }

}
