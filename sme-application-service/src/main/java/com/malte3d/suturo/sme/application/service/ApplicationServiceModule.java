package com.malte3d.suturo.sme.application.service;

import com.google.inject.AbstractModule;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;

import javax.inject.Singleton;

public class ApplicationServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SettingsService.class).in(Singleton.class);
    }
}
