package com.malte3d.suturo.commons.ddd.event.integration;

import java.util.function.Consumer;

import lombok.NonNull;

public class BasicIntegrationEventConsumer<T extends IntegrationEvent> implements IntegrationEventConsumer<T> {

    private final Class<T>    eventClass;
    private final Consumer<T> eventConsumer;

    public BasicIntegrationEventConsumer(@NonNull Class<T> eventClass, @NonNull Consumer<T> eventConsumer) {
        this.eventClass = eventClass;
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void reactToEvent(@NonNull T event) {
        eventConsumer.accept(event);
    }

    @Override
    public boolean acceptsEvent(@NonNull IntegrationEvent event) {
        return eventClass.isAssignableFrom(event.getClass());
    }
}
