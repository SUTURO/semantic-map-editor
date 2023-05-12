package com.malte3d.suturo.sme.domain.model.application.settings.advanced;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Event that is raised when the debug mode has changed.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class DebugModeChangedEvent extends DomainEvent {

    DebugMode newDebugMode;

}
