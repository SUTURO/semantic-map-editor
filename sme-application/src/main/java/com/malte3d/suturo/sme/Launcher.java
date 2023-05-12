package com.malte3d.suturo.sme;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.malte3d.suturo.commons.messages.Language;
import com.malte3d.suturo.sme.adapter.integration.IntegrationAdapterModule;
import com.malte3d.suturo.sme.adapter.persistence.PersistenceAdapterModule;
import com.malte3d.suturo.sme.application.service.ApplicationServiceModule;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.service.DomainServiceModule;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
import com.malte3d.suturo.sme.ui.MainApplication;
import com.malte3d.suturo.sme.ui.MainApplicationOptions;
import com.malte3d.suturo.sme.ui.UiModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Set;

@Slf4j
@UtilityClass
public class Launcher {

    public static void main(String[] args) {

        Locale.setDefault(Language.ENGLISH.locale);

        Set<Module> applicationModules = createApplicationModules();
        Injector injector = Guice.createInjector(applicationModules);

        MainApplicationOptions options = createMainApplicationOptions(injector, args);

        MainApplication.launch(options);
    }

    private static MainApplicationOptions createMainApplicationOptions(Injector injector, String[] args) {

        LauncherOptions options = LauncherOptions.parse(args);

        SettingsRepository settingsRepository = injector.getInstance(SettingsRepository.class);
        Settings settings = settingsRepository.load();
        Settings newSettings = settings.withAdvancedSettings(settings.getAdvancedSettings().withDebugMode(DebugMode.of(options.isDebugMode())));

        settingsRepository.save(newSettings);

        return new MainApplicationOptions(injector, args);
    }

    private static Set<Module> createApplicationModules() {

        return Set.of(
                new ApplicationServiceModule(),
                new DomainServiceModule(),
                new PersistenceAdapterModule(),
                new IntegrationAdapterModule(),
                new UiModule()
        );
    }
}
