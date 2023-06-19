package com.malte3d.suturo.sme.ui.viewmodel.editor.event;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Raised when the state of the camera changes.
 */
@ValueObject

@Value
@EqualsAndHashCode(callSuper = true)
public class CameraStateChangedEvent extends DomainEvent {

    boolean isActive;

}
