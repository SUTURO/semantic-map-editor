package com.malte3d.suturo.commons.ddd.event.domain;

import java.time.Instant;

import lombok.Getter;

/**
 * Marker class for a domain event. The implementation should be immutable.
 */
public abstract class DomainEvent {

    @Getter
    private final Instant timestamp = Instant.now();

}
