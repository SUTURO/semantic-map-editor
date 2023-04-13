package com.malte3d.suturo.sme.adapter.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.malte3d.suturo.sme.adapter.persistence.application.settings.SettingsRepositoryCache;
import com.malte3d.suturo.sme.adapter.persistence.application.settings.SettingsRepositoryImpl;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;

import javax.inject.Singleton;

public class PersistenceAdapterModule extends AbstractModule {

    @Provides
    @Singleton
    public SettingsRepository settingsRepository() {
        return new SettingsRepositoryCache(new SettingsRepositoryImpl());
    }
    
}
