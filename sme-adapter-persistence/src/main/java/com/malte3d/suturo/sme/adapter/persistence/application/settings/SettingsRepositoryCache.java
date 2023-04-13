package com.malte3d.suturo.sme.adapter.persistence.application.settings;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
import lombok.NonNull;

import javax.inject.Inject;

public class SettingsRepositoryCache implements SettingsRepository {

    private static final String DUMMY_KEY = "Settings";

    private final SettingsRepository delegate;

    private final LoadingCache<String, Settings> settingsCache;

    @Inject
    public SettingsRepositoryCache(@NonNull SettingsRepository delegate) {

        this.delegate = delegate;

        settingsCache = CacheBuilder.newBuilder()
                .maximumSize(1)
                .build(new CacheLoader<>() {
                    @Override
                    public @NonNull Settings load(@NonNull String key) {
                        return delegate.load();
                    }
                });
    }

    @Override
    public Settings load() {
        return settingsCache.getUnchecked(DUMMY_KEY);
    }

    @Override
    public void save(@NonNull Settings settings) {
        settingsCache.put(DUMMY_KEY, settings);
        delegate.save(settings);
    }
}
