package com.malte3d.suturo.sme.domain.service;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;

@SuppressWarnings("MethodMayBeStatic")
public class DomainServiceModule extends AbstractModule {

    @Singleton
    @Provides
    DomainEventPublisher domainEventPublisher() {
        return new DomainEventPublisher();
    }
}
