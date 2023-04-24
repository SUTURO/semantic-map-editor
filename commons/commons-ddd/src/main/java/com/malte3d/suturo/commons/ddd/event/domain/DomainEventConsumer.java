package com.malte3d.suturo.commons.ddd.event.domain;

import lombok.NonNull;

import java.util.function.Consumer;

/**
 * Interface to enable a class to react to a domain event.
 *
 * @param <T> A domain event class
 */
public interface DomainEventConsumer<T extends DomainEvent> {

    static <T extends DomainEvent> DomainEventConsumer<T> of(Class<T> eventClass, Consumer<T> eventConsumer) {
        return new BasicDomainEventConsumer<>(eventClass, eventConsumer);
    }

    static <T extends DomainEvent> DomainEventConsumer<T> of(Class<T> eventClass, Runnable runnable) {
        return new BasicDomainEventConsumer<>(eventClass, event -> runnable.run());
    }

    void reactToEvent(@NonNull T event);

    /**
     * This implementation should be resource-efficient. E.g. {@code ExampleDomainEvent.class.equals(event.getClass())}
     *
     * @param event The {@link DomainEvent}
     * @return True, if this consumer accepts the passed event and reacts to it
     */
    boolean acceptsEvent(@NonNull DomainEvent event);

}
