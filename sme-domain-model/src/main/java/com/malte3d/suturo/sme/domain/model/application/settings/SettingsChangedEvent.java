package com.malte3d.suturo.sme.domain.model.application.settings;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Event that is raised when the application settings have changed.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SettingsChangedEvent extends DomainEvent {

    Settings newSettings;

}
