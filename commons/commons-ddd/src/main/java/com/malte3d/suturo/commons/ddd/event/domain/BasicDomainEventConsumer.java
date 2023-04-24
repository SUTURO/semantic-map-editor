package com.malte3d.suturo.commons.ddd.event.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
final class BasicDomainEventConsumer<T extends DomainEvent> implements DomainEventConsumer<T> {

    private final Class<T> eventClass;
    private final Consumer<T> eventConsumer;

    @Override
    public void reactToEvent(@NonNull T event) {
        eventConsumer.accept(event);
    }

    @Override
    public boolean acceptsEvent(@NonNull DomainEvent event) {
        return eventClass.isAssignableFrom(event.getClass());
    }
}
