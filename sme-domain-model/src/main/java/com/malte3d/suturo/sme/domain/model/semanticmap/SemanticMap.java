package com.malte3d.suturo.sme.domain.model.semanticmap;

import com.malte3d.suturo.commons.ddd.annotation.AggregateRoot;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.Node;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general.NullObject;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Represents a semantic map.
 *
 * <p>
 * The semantic map holds a scene graph of {@link SmObject}s. The semantic map objects follow the <a href="https://www.ros.org/reps/rep-0103.html">ROS convention</a> for coordinate systems and units.
 * </p>
 * <p>
 * <strong>Coordinate System:</strong>
 * <br>
 * The coordinate system is right-handed with the z-axis pointing upwards.
 * </p>
 * <p>
 * <strong>Base Units:</strong>
 * <br>
 * length: meter
 * <br>
 * angle: radian
 * </p>
 */
@AggregateRoot

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SemanticMap {

    @NonNull
    @EqualsAndHashCode.Include
    SemanticMapIdentifier id;

    @NonNull
    SemanticMapName name;

    @NonNull
    Node<SmObject> scenegraph;

    /**
     * Creates a new default semantic map.
     *
     * @param name the name of the semantic map
     * @return a new semantic map with the given name
     */
    public static SemanticMap create(@NonNull SemanticMapName name) {
        return new SemanticMap(
                SemanticMapIdentifier.create(),
                name,
                new Node<>(new NullObject(SmObjectName.of(name.getValue()), Position.ZERO, Rotation.IDENTITY))
        );
    }

}
