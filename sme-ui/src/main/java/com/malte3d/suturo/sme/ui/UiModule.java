package com.malte3d.suturo.sme.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.malte3d.suturo.commons.javafx.fxml.FxmlViewFactory;
import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import javafx.application.HostServices;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(HostServices.class).toProvider(MainApplication.class).in(Singleton.class);
        bind(Executor.class).annotatedWith(GlobalExecutor.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        bind(FxmlViewFactory.class).to(MainApplicationViewFactory.class).in(Singleton.class);
    }

}
