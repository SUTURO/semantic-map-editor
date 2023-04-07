package com.malte3d.suturo.commons.ddd.event.integration;

import java.util.function.Consumer;

import lombok.NonNull;

/**
 * Interface to enable a class to react to an integration event.
 *
 * @param <T> An integration event class
 */
public interface IntegrationEventConsumer<T extends IntegrationEvent> {

    static <T extends IntegrationEvent> IntegrationEventConsumer<T> of(Class<T> eventClass, Consumer<T> eventConsumer) {
        return new BasicIntegrationEventConsumer<>(eventClass, eventConsumer);
    }

    static <T extends IntegrationEvent> IntegrationEventConsumer<T> of(Class<T> eventClass, Runnable runnable) {
        return new BasicIntegrationEventConsumer<>(eventClass, event -> runnable.run());
    }

    void reactToEvent(@NonNull T event);

    /**
     * This implementation should be resource-efficient. E.g. ExampleIntegrationEvent.getClass().equals(event.getClass())
     *
     * @param event The {@link IntegrationEvent}
     * @return True, if this consumer accepts the passed event and reacts to it
     */
    boolean acceptsEvent(@NonNull IntegrationEvent event);

}
