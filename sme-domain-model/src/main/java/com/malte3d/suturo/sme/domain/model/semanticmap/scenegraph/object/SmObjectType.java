package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.messages.Messages;
import lombok.RequiredArgsConstructor;

@ValueObject

@RequiredArgsConstructor
public enum SmObjectType {

    NULL(1),
    BOX(2),
    CYLINDER(3),
    SPHERE(4),
    PLANE(5),
    IMPORT(6);

    public final int eternalId;

    public String getEternalName() {
        return String.valueOf(eternalId);
    }

    public static SmObjectType of(int eternalId) {

        for (SmObjectType elem : values())
            if (elem.eternalId == eternalId)
                return elem;

        throw new UnsupportedEnumException(Messages.format("{} for {} does not exist!", SmObjectType.class.getSimpleName(), eternalId));
    }
}
