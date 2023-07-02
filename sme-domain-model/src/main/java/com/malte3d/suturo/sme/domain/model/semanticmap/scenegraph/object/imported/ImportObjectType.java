package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.i18n.I18N;
import com.malte3d.suturo.commons.messages.Messages;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The type of the imported object.
 */
@ValueObject

@RequiredArgsConstructor
public enum ImportObjectType {

    @I18N("Editor.ImportObjectType.OBJ")
    OBJ(1),

    @I18N("Editor.ImportObjectType.GLB")
    GLB(2),

    @I18N("Editor.ImportObjectType.BLEND")
    BLEND(3);

    public final int eternalId;

    public static ImportObjectType of(int eternalId) {

        for (ImportObjectType elem : values())
            if (elem.eternalId == eternalId)
                return elem;

        throw new UnsupportedEnumException(Messages.format("{} for {} does not exist!", ImportObjectType.class.getSimpleName(), eternalId));
    }

    public static ImportObjectType of(@NonNull String fileExtension) {

        return switch (fileExtension) {
            case "obj" -> OBJ;
            case "glb" -> GLB;
            case "blend" -> BLEND;
            default ->
                    throw new UnsupportedEnumException(Messages.format("{} for {} does not exist!", ImportObjectType.class.getSimpleName(), fileExtension));
        };
    }

}
