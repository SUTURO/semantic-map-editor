package com.malte3d.suturo.sme;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.malte3d.suturo.sme.adapter.integration.IntegrationAdapterModule;
import com.malte3d.suturo.sme.adapter.persistence.PersistenceAdapterModule;
import com.malte3d.suturo.sme.application.service.ApplicationServiceModule;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.service.DomainServiceModule;
import com.malte3d.suturo.sme.ui.InjectorProvider;
import com.malte3d.suturo.sme.ui.MainApplication;
import com.malte3d.suturo.sme.ui.UiModule;
import javafx.application.Application;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Set;

@Slf4j
@UtilityClass
public class Launcher {

    public static void main(String[] args) {

        initInjector();
        initSettings(args);

        Application.launch(MainApplication.class, args);
    }

    /**
     * Loads and initializes the settings.
     *
     * <p>
     * The settings may be overridden by command line arguments.
     * </p>
     *
     * @param args The command line arguments.
     */
    private static void initSettings(String[] args) {

        LauncherOptions launcherOptions = LauncherOptions.parse(args);

        SettingsService settingsService = InjectorProvider.get().getInstance(SettingsService.class);
        Settings settings = settingsService.get();

        Settings newSettings = settings.withAdvanced(settings.getAdvanced().withDebugMode(DebugMode.of(launcherOptions.isDebugMode())));
        settingsService.save(newSettings);

        Locale.setDefault(settings.getAppearance().getLanguage().locale);
    }

    private static void initInjector() {

        Set<Module> applicationModules = Set.of(
                new ApplicationServiceModule(),
                new DomainServiceModule(),
                new PersistenceAdapterModule(),
                new IntegrationAdapterModule(),
                new UiModule()
        );

        Injector injector = Guice.createInjector(applicationModules);

        InjectorProvider.setInjector(injector);
    }
}
