package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents a primitive sphere.
 */
@ValueObject

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString
public class Sphere extends Primitive {

    public static final Sphere DEFAULT = new Sphere(SmObjectName.of("Sphere"), Position.ZERO, Rotation.IDENTITY, 1.0f);

    /**
     * The radius of the sphere in meter.
     */
    float radius;

    public Sphere(@NonNull SmObjectName name, @NonNull Position position, @NonNull Rotation rotation, float radius) {
        super(name, position, rotation);
        this.radius = radius;
    }
}
