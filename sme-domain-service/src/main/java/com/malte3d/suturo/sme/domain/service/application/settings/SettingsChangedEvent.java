package com.malte3d.suturo.sme.domain.service.application.settings;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SettingsChangedEvent extends DomainEvent {

    Settings newSettings;
    
}
