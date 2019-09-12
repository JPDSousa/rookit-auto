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
package org.rookit.auto.javax.repetition;

import com.google.inject.Inject;
import org.rookit.auto.javax.ElementUtils;
import org.rookit.utils.guice.Keyed;
import org.rookit.utils.guice.Multi;
import org.rookit.utils.guice.Optional;
import org.rookit.utils.guice.Single;

import javax.annotation.processing.Messager;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Collection;
import java.util.Set;

import static java.lang.String.format;

public final class BaseJavaxRepetitionFactory implements JavaxRepetitionFactory {

    public static JavaxRepetitionFactory create(
            final Messager messager,
            final Types types,
            final ElementUtils elementUtils,
            final Set<TypeMirrorRepetitionConfig> optionalTypes,
            final Set<TypeMirrorRepetitionConfig> collectionTypes,
            final Set<TypeMirrorKeyedRepetitionConfig> mapTypes,
            final TypeMirrorConfig single) {
        return new BaseJavaxRepetitionFactory(messager, types, elementUtils, optionalTypes, collectionTypes,
                mapTypes, single);
    }

    private final Messager messager;
    private final Types types;

    private final Collection<TypeMirrorRepetitionConfig> optionalTypes;
    private final Collection<TypeMirrorRepetitionConfig> collectionTypes;
    private final Collection<TypeMirrorKeyedRepetitionConfig> mapTypes;
    private final TypeMirrorConfig single;

    @SuppressWarnings("TypeMayBeWeakened")
    @Inject
    private BaseJavaxRepetitionFactory(
            final Messager messager,
            final Types types,
            final ElementUtils elementUtils,
            @Optional final Set<TypeMirrorRepetitionConfig> optionalTypes,
            @Multi final Set<TypeMirrorRepetitionConfig> collectionTypes,
            @Keyed final Set<TypeMirrorKeyedRepetitionConfig> mapTypes,
            @Single final TypeMirrorConfig single) {
        final Collection<TypeMirror> commonTypes = elementUtils.intersectionConfigs(optionalTypes, collectionTypes);
        if (!commonTypes.isEmpty()) {
            final String errorMessage = format("Types %s are both registered as optional as " +
                    "well as collection types. They can only be registered as either optional (0..1) " +
                    "or collection (0..n).", commonTypes);
            throw new IllegalArgumentException(errorMessage);
        }

        this.types = types;
        this.mapTypes = mapTypes;
        this.optionalTypes = optionalTypes;
        this.collectionTypes = collectionTypes;
        this.messager = messager;
        this.single = single;
    }

    // TODO break me down into pieces
    @Override
    public GenericTypeMirrorConfig fromTypeMirror(final TypeMirror typeMirror) {
        final String inferMessage = "Infering %s as %s";
        final TypeMirror typeErasured = this.types.erasure(typeMirror);

        // for optionals
        for (final TypeMirrorRepetitionConfig optionalType : this.optionalTypes) {
            if (this.types.isSameType(optionalType.typeMirror(), typeErasured)) {
                this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, "Optional"));
                return optionalType;
            }
        }

        // for collections
        for (final TypeMirrorRepetitionConfig collectionType : this.collectionTypes) {
            if (this.types.isSameType(collectionType.typeMirror(), typeErasured)) {
                this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, "Multi"));
                return collectionType;
            }
        }

        // for maps
        for (final TypeMirrorKeyedRepetitionConfig mapType : this.mapTypes) {
            if (this.types.isSameType(mapType.typeMirror(), typeErasured)) {
                this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, "Keyed"));
                return mapType;
            }
        }
        this.messager.printMessage(Diagnostic.Kind.NOTE, format(inferMessage, typeMirror, this.single));
        return this.single;
    }

    @Override
    public String toString() {
        return "BaseJavaxRepetitionFactory{" +
                "messager=" + this.messager +
                ", types=" + this.types +
                ", optionalTypes=" + this.optionalTypes +
                ", collectionTypes=" + this.collectionTypes +
                ", mapTypes=" + this.mapTypes +
                ", single=" + this.single +
                "}";
    }
}
