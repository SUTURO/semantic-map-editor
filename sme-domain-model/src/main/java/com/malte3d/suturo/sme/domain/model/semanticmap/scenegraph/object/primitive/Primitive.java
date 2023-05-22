package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive;

import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Base class for all primitive objects.
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class Primitive extends SmObject {

    protected Primitive(@NonNull SmObjectName name, @NonNull Position position, @NonNull Rotation rotation) {
        super(name, position, rotation);
    }
}
