package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.messages.Messages;
import lombok.RequiredArgsConstructor;

/**
 * The type of the imported object.
 */
@ValueObject

@RequiredArgsConstructor
public enum ImportObjectType {

    OBJ(1),
    GLB(2);

    public final int eternalId;

    public static ImportObjectType of(int eternalId) {

        for (ImportObjectType elem : values())
            if (elem.eternalId == eternalId)
                return elem;

        throw new UnsupportedEnumException(Messages.format("{} for {} does not exist!", ImportObjectType.class.getSimpleName(), eternalId));
    }

}
