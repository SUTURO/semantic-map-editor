package com.malte3d.suturo.commons.ddd.event.domain;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import lombok.NonNull;

/**
 * At the {@link DomainEventPublisher}, {@link DomainEventConsumer EventConsumer} can register to process {@link DomainEvent DomainEvents}. Every bounded
 * context should maintain exactly one publisher instance as singleton.
 */
public final class DomainEventPublisher {

    private final Set<DomainEventConsumer<? extends DomainEvent>> domainEventConsumers = ConcurrentHashMap.newKeySet();

    /**
     * Adds the passed consumer
     *
     * @param consumer The consumer to add
     */
    public void register(@NonNull DomainEventConsumer<? extends DomainEvent> consumer) {
        domainEventConsumers.add(consumer);
    }

    /**
     * Adds the passed consumer
     */
    public <T extends DomainEvent> void register(@NonNull Class<T> eventClass, @NonNull Consumer<T> eventConsumer) {
        domainEventConsumers.add(new BasicDomainEventConsumer<>(eventClass, eventConsumer));
    }

    /**
     * Adds the passed runnable
     */
    public <T extends DomainEvent> void register(@NonNull Class<T> eventClass, @NonNull Runnable eventRunnable) {
        domainEventConsumers.add(new BasicDomainEventRunnable<>(eventClass, eventRunnable));
    }

    /**
     * Removes the passed consumer
     *
     * @param consumer The consumer to be removed
     */
    public void remove(@NonNull DomainEventConsumer<? extends DomainEvent> consumer) {
        domainEventConsumers.remove(consumer);
    }

    /**
     * Removes all {@link DomainEventConsumer consumers}
     */
    public void clearConsumers() {
        domainEventConsumers.clear();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void raise(@NonNull DomainEvent event) {

        for (DomainEventConsumer consumer : domainEventConsumers) {

            if (consumer.acceptsEvent(event))
                consumer.reactToEvent(event);
        }
    }
}
