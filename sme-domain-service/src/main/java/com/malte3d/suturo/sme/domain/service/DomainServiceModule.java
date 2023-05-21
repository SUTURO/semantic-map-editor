package com.malte3d.suturo.sme.domain.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;

import javax.inject.Singleton;

@SuppressWarnings("MethodMayBeStatic")
public class DomainServiceModule extends AbstractModule {

    @Singleton
    @Provides
    DomainEventHandler domainEventPublisher() {
        return new DomainEventHandler();
    }
}
