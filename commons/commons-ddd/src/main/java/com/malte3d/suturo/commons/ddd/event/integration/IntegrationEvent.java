package com.malte3d.suturo.commons.ddd.event.integration;

import java.time.Instant;

import lombok.Getter;

/**
 * Marker class for an integration event. The implementation should be immutable.
 */
public abstract class IntegrationEvent {

    @Getter
    private final Instant timestamp = Instant.now();

}
