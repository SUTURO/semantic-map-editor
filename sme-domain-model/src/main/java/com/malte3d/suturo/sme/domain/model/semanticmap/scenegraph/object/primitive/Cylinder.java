package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a primitive cylinder.
 */
@ValueObject

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString
public class Cylinder extends Primitive {

    public static final Cylinder DEFAULT = new Cylinder(SmObjectName.of("Cylinder"), Position.of(0f, 0f, 0f), Rotation.IDENTITY, 1.0f, 0.25f);

    /**
     * The height of the cylinder in meter.
     */
    float height;

    /**
     * The radius of the cylinder in meter.
     */
    float radius;

    public Cylinder(@NonNull SmObjectName name, @NonNull Position position, @NonNull Rotation rotation, float height, float radius) {
        super(name, SmObjectType.CYLINDER, position, rotation);
        this.height = height;
        this.radius = radius;
    }
}
