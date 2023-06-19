package com.malte3d.suturo.sme.ui.viewmodel.editor.event;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.transform.TransformMode;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Raised when the transform mode has been changed.
 */
@ValueObject

@Value
@EqualsAndHashCode(callSuper = true)
public class TransformModeChangedEvent extends DomainEvent {

    /**
     * The new transform mode.
     */
    @NonNull
    TransformMode transformMode;

}
