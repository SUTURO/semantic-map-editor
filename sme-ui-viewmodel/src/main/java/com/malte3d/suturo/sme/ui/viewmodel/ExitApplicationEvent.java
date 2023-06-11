package com.malte3d.suturo.sme.ui.viewmodel;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Raised when the application should be exited.
 * <p>
 * This usually happens when the user clicks on the "Exit" menu item.
 * </p>
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ExitApplicationEvent extends DomainEvent {
}
