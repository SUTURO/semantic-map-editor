package com.malte3d.suturo.commons.ddd.event.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class BasicDomainEventRunnable<T extends DomainEvent> implements DomainEventConsumer<T> {

    private final Class<T> eventClass;
    private final Runnable eventRunnable;

    @Override
    public void reactToEvent(@NonNull T event) {
        eventRunnable.run();
    }

    @Override
    public boolean acceptsEvent(@NonNull DomainEvent event) {
        return eventClass.isAssignableFrom(event.getClass());
    }
}
