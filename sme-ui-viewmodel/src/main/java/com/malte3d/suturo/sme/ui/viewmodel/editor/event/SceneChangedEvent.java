package com.malte3d.suturo.sme.ui.viewmodel.editor.event;

import com.jme3.scene.Node;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Raised when the complete scene graph has been changed.
 *
 * <p>
 * Usually this event is raised when the user has loaded a new semantic map.
 * </p>
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SceneChangedEvent extends DomainEvent {

    Node scenegraph;

}
