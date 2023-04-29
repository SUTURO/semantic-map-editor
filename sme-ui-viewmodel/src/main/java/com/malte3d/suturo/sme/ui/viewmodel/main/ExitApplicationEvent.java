package com.malte3d.suturo.sme.ui.viewmodel.main;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ExitApplicationEvent extends DomainEvent {
}
