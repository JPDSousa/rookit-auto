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
package org.rookit.auto.javax.pack;

import com.google.inject.Inject;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.rookit.auto.javax.naming.NameFactory;
import org.rookit.utils.collection.ListUtils;
import org.rookit.utils.registry.Registry;
import org.rookit.utils.string.join.JointString;

import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import java.util.Objects;

final class PackageElementRegistry implements Registry<JointString, PackageElement> {

    private final Elements elements;
    private final Elements fallbackElements;
    private final NameFactory nameFactory;
    private final ListUtils listUtils;

    @Inject
    private PackageElementRegistry(
            final Elements elements,
            final Elements fallbackElements,
            final NameFactory nameFactory,
            final ListUtils listUtils) {
        this.elements = elements;
        this.fallbackElements = fallbackElements;
        this.nameFactory = nameFactory;
        this.listUtils = listUtils;
    }

    @Override
    public Maybe<PackageElement> get(final JointString key) {
        return fetch(key).toMaybe();
    }

    @Override
    public Single<PackageElement> fetch(final JointString key) {
        final PackageElement packageElement = this.elements.getPackageElement(key.asString());
        if (Objects.isNull(packageElement)) {
            return Single.just(this.fallbackElements.getPackageElement(key.asString()));
        }

        return Single.just(packageElement);
    }

    @Override
    public void close() {
        // nothing to be closed
    }

    @Override
    public String toString() {
        return "PackageElementRegistry{" +
                "elements=" + this.elements +
                ", fallbackElements=" + this.fallbackElements +
                ", nameFactory=" + this.nameFactory +
                ", listUtils=" + this.listUtils +
                "}";
    }

}
