package com.malte3d.suturo.sme.ui.viewmodel.editor.event;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Raised when the editor has been initialized.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class EditorInitializedEvent extends DomainEvent {
}
