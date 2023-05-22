package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Base class for all semantic map objects.
 *
 * <p>
 * Every object in the semantic map has at least a position and a rotation.
 * </p>
 */
@ValueObject

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public abstract class SmObject {

    @NonNull
    SmObjectName name;

    @NonNull
    Position position;

    @NonNull
    Rotation rotation;

}
