package com.malte3d.suturo.sme;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.malte3d.suturo.commons.javafx.FxmlLoaderUtil;
import com.malte3d.suturo.commons.messages.Language;
import com.malte3d.suturo.sme.adapter.integration.IntegrationAdapterModule;
import com.malte3d.suturo.sme.adapter.persistence.PersistenceAdapterModule;
import com.malte3d.suturo.sme.application.service.ApplicationServiceModule;
import com.malte3d.suturo.sme.domain.service.DomainServiceModule;
import com.malte3d.suturo.sme.ui.MainApplication;
import com.malte3d.suturo.sme.ui.UiModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Set;

@Slf4j
@UtilityClass
public class Launcher {

    public static void main(String[] args) {

        Set<Module> applicationModules = createApplicationModules();
        Injector injector = Guice.createInjector(applicationModules);

        FxmlLoaderUtil.init(injector);
        Locale.setDefault(Language.ENGLISH.locale);

        MainApplication application = injector.getInstance(MainApplication.class);
        application.run(args);
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
