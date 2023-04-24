package com.malte3d.suturo.commons.ddd.event.domain;

import java.util.function.Consumer;

import lombok.NonNull;

public class BasicDomainEventConsumer<T extends DomainEvent> implements DomainEventConsumer<T> {

    private final Class<T> eventClass;
    private final Consumer<T> eventConsumer;

    public BasicDomainEventConsumer(@NonNull Class<T> eventClass, @NonNull Consumer<T> eventConsumer) {
        this.eventClass = eventClass;
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void reactToEvent(@NonNull T event) {
        eventConsumer.accept(event);
    }

    @Override
    public boolean acceptsEvent(@NonNull DomainEvent event) {
        return eventClass.isAssignableFrom(event.getClass());
    }
}
