package com.malte3d.suturo.sme.ui;

import com.google.inject.AbstractModule;
import com.malte3d.suturo.commons.javafx.GlobalExecutor;
import com.malte3d.suturo.sme.ui.service.MainApplicationUiService;
import javafx.application.HostServices;

import javax.inject.Singleton;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(MainApplicationUiService.class).in(Singleton.class);

        bind(HostServices.class).toProvider(MainApplication.class);
        bind(Executor.class).annotatedWith(GlobalExecutor.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

}
