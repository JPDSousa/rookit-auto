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
package org.rookit.auto.javax;

import com.google.inject.Inject;
import org.rookit.auto.guice.Map;
import org.rookit.auto.guice.Optional;
import org.rookit.auto.javax.element.ElementUtils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.Set;

import static java.lang.String.format;

public final class BaseJavaxRepetitionFactory implements JavaxRepetitionFactory {

    public static JavaxRepetitionFactory create(final Messager messager,
                                                final ElementUtils utils,
                                                final Set<RepetitiveTypeMirror> optionalTypes,
                                                final Set<RepetitiveTypeMirror> collectionTypes,
                                                final Set<KeyedRepetitiveTypeMirror> mapTypes) {
        return new BaseJavaxRepetitionFactory(messager, utils, optionalTypes, collectionTypes, mapTypes);
    }

    private final Messager messager;
    private final JavaxRepetition single;

    private final Collection<RepetitiveTypeMirror> optionalTypes;
    private final Collection<RepetitiveTypeMirror> collectionTypes;
    private final Collection<KeyedRepetitiveTypeMirror> mapTypes;

    @SuppressWarnings("TypeMayBeWeakened")
    @Inject
    private BaseJavaxRepetitionFactory(
            final Messager messager,
            final ElementUtils utils,
            @Optional final Set<RepetitiveTypeMirror> optionalTypes,
            @org.rookit.auto.guice.Collection final Set<RepetitiveTypeMirror> collectionTypes,
            @Map final Set<KeyedRepetitiveTypeMirror> mapTypes) {
        this.mapTypes = mapTypes;
        final Collection<RepetitiveTypeMirror> commonTypes = utils.intersection(optionalTypes, collectionTypes);
        if (!commonTypes.isEmpty()) {
            final String errorMessage = format("Types %s are both registered as optional as " +
                    "well as collection types. They can only be registered as either optional (0..1) " +
                    "or collection (0..n).", commonTypes);
            throw new IllegalArgumentException(errorMessage);
        }

        this.messager = messager;
        this.optionalTypes = optionalTypes;
        this.collectionTypes = collectionTypes;
        this.single = new SingleRepetition();
    }

    // TODO break me down into pieces
    @Override
    public JavaxRepetition fromTypeMirror(final ExtendedTypeMirror typeMirror) {
        final String inferMessage = "Infering %s as %s";

        // for optionals
        for (final RepetitiveTypeMirror optionalType : this.optionalTypes) {
            if (optionalType.isSameTypeErasure(typeMirror)) {
                this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, "Optional"));
                return new BaseRepetition("Optional", this.messager, false, true, optionalType);
            }
        }

        // for collections
        for (final RepetitiveTypeMirror collectionType : this.collectionTypes) {
            if (collectionType.isSameTypeErasure(typeMirror)) {
                this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, "Multi"));
                return new BaseRepetition("Multi", this.messager, true, true, collectionType);
            }
        }

        // for maps
        for (final KeyedRepetitiveTypeMirror mapType : this.mapTypes) {
            if (mapType.isSameTypeErasure(typeMirror)) {
                this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, "Map"));
                return new KeyedRepetitionImpl("Map", this.messager, true, true, mapType);
            }
        }
        this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, this.single));
        return this.single;
    }

    @Override
    public String toString() {
        return "BaseJavaxRepetitionFactory{" +
                "messager=" + this.messager +
                ", single=" + this.single +
                ", optionalTypes=" + this.optionalTypes +
                ", collectionTypes=" + this.collectionTypes +
                ", mapTypes=" + this.mapTypes +
                "}";
    }
}
