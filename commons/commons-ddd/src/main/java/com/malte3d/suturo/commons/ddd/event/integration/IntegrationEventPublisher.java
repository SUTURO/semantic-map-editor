package com.malte3d.suturo.commons.ddd.event.integration;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * At the {@link IntegrationEventPublisher}, {@link IntegrationEventConsumer EventConsumer} can register to process {@link IntegrationEvent IntegrationEvents}. Every bounded
 * context should maintain exactly one publisher instance as singleton.
 */
@RequiredArgsConstructor
public final class IntegrationEventPublisher {

    private final Set<IntegrationEventConsumer<? extends IntegrationEvent>> integrationEventConsumers = ConcurrentHashMap.newKeySet();

    @NonNull
    private final Executor executor;

    /**
     * Adds the passed consumer
     *
     * @param consumer The consumer to add
     */
    public void register(@NonNull IntegrationEventConsumer<? extends IntegrationEvent> consumer) {
        integrationEventConsumers.add(consumer);
    }

    /**
     * Removes the passed consumer
     *
     * @param consumer The consumer to be removed
     */
    public void remove(@NonNull IntegrationEventConsumer<? extends IntegrationEvent> consumer) {
        integrationEventConsumers.remove(consumer);
    }

    /**
     * Removes all {@link IntegrationEventConsumer consumers}
     */
    public void clearConsumers() {
        integrationEventConsumers.clear();
    }

    /**
     * Raises the passed {@link IntegrationEvent} <i>asynchronously</i> to all registered {@link IntegrationEventConsumer} that react to this event type.
     *
     * @param event The {@link IntegrationEvent}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void raise(@NonNull IntegrationEvent event) {

        executor.execute(() -> {

            for (IntegrationEventConsumer consumer : integrationEventConsumers) {

                if (consumer.acceptsEvent(event))
                    consumer.reactToEvent(event);
            }
        });
    }
}
