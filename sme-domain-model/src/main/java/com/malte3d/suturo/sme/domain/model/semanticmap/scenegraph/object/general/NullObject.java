package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * A null object.
 *
 * <p>
 * Can be used to group objects in the scene graph.
 * </p>
 */
@ValueObject

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NullObject extends SmObject {

    public static final NullObject DEFAULT = new NullObject(SmObjectName.of("Null"), Position.ZERO, Rotation.IDENTITY);

    /**
     * The root object of the scene graph.
     */
    public static final NullObject ROOT = new NullObject(SmObjectName.of("ROOT"), Position.ZERO, Rotation.IDENTITY);

    public NullObject(@NonNull SmObjectName name, @NonNull Position position, @NonNull Rotation rotation) {
        super(name, SmObjectType.NULL, position, rotation);
    }
}
