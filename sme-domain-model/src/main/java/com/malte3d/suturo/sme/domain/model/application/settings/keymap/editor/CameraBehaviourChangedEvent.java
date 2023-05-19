package com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Event that is raised when the camera behaviour mode has changed.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class CameraBehaviourChangedEvent extends DomainEvent {

    CameraBehaviour newCameraBehaviour;

}
