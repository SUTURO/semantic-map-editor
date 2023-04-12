package com.malte3d.suturo.sme.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.malte3d.suturo.commons.javafx.GlobalExecutor;
import com.malte3d.suturo.sme.ui.service.MainApplicationUiService;
import javafx.fxml.FXMLLoader;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(MainController.class).in(Singleton.class);
        bind(MainApplicationUiService.class).in(Singleton.class);

        bind(Executor.class).annotatedWith(GlobalExecutor.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

    @Provides
    public FXMLLoader fxmlloader(Injector injector) {

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);

        return loader;
    }
}
