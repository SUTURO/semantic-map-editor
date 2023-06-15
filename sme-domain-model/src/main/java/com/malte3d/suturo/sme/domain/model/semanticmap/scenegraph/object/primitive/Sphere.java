package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
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

    public static final Sphere DEFAULT = new Sphere(SmObjectName.of("Sphere"), Position.of(0, 0.5f, 0), Rotation.IDENTITY, 0.5f);

    /**
     * The radius of the sphere in meter.
     */
    float radius;

    public Sphere(@NonNull SmObjectName name, @NonNull Position position, @NonNull Rotation rotation, float radius) {
        super(name, SmObjectType.SPHERE, position, rotation);
        this.radius = radius;
    }
}
