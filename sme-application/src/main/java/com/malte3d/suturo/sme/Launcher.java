package com.malte3d.suturo.sme;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.malte3d.suturo.commons.messages.Language;
import com.malte3d.suturo.sme.adapter.integration.IntegrationAdapterModule;
import com.malte3d.suturo.sme.adapter.persistence.PersistenceAdapterModule;
import com.malte3d.suturo.sme.application.service.ApplicationServiceModule;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.service.DomainServiceModule;
import com.malte3d.suturo.sme.ui.InjectorContext;
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

        Locale.setDefault(Language.ENGLISH.locale);

        Set<Module> applicationModules = createApplicationModules();
        InjectorContext.injector = Guice.createInjector(applicationModules);

        parseLauncherOptions(args);

        Application.launch(MainApplication.class, args);
    }

    /**
     * Parses and applies the launcher options.
     *
     * @param args The command line arguments.
     */
    private static void parseLauncherOptions(String[] args) {

        LauncherOptions options = LauncherOptions.parse(args);

        SettingsService settingsService = InjectorContext.injector.getInstance(SettingsService.class);
        Settings settings = settingsService.get();
        Settings newSettings = settings.withAdvanced(settings.getAdvanced().withDebugMode(DebugMode.of(options.isDebugMode())));

        settingsService.save(newSettings);
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
