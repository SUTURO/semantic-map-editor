package com.malte3d.suturo.sme.ui.viewmodel.editor.event;

import com.jme3.scene.Spatial;
import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

/**
 * Raised when the user selects an object in the scene.
 */
@ValueObject

@Value
@EqualsAndHashCode(callSuper = true)
public class ObjectSelectedEvent extends DomainEvent {

    /**
     * The selected object.
     *
     * <p>
     * This is an optional because the user can also deselect an object.
     * </p>
     */
    @NonNull
    Optional<Spatial> selectedObject;

    Origin origin;

    public enum Origin {

        SCENEGRAPH_VIEW,
        EDITOR

    }
}
