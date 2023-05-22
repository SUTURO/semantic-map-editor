package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.Value;

/**
 * Position of an object in 3D space.
 *
 * <p>Represented as a 3 dimensional vector.</p>
 */
@ValueObject

@Value(staticConstructor = "of")
public class Position {

    public static final Position ZERO = Position.of(0, 0, 0);
    public static final Position UNIT_X = Position.of(1, 0, 0);
    public static final Position UNIT_Y = Position.of(0, 1, 0);
    public static final Position UNIT_Z = Position.of(0, 0, 1);
    public static final Position UNIT_XYZ = Position.of(1, 1, 1);

    /**
     * The forward position.
     */
    float x;

    /**
     * The left position.
     */
    float y;

    /**
     * The up position.
     */
    float z;

}
