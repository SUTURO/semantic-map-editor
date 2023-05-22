package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.Value;

/**
 * Rotation of an object in 3D space.
 *
 * <p>Represented as a quaternion.</p>
 */
@ValueObject

@Value(staticConstructor = "of")
public class Rotation {

    /**
     * Identity quaternion, that represents no rotation.
     */
    public static final Rotation IDENTITY = Rotation.of(1, 0, 0, 0);
    public static final Rotation FLIP_X = Rotation.of(0, 1, 0, 0);
    public static final Rotation FLIP_Y = Rotation.of(0, 0, 1, 0);
    public static final Rotation FLIP_Z = Rotation.of(0, 0, 0, 1);

    float x;
    float y;
    float z;
    float w;

}
