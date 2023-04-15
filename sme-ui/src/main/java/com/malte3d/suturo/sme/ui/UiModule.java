package com.malte3d.suturo.sme.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.malte3d.suturo.commons.javafx.GlobalExecutor;
import com.malte3d.suturo.sme.ui.service.MainApplicationUiService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(MainApplicationUiService.class).in(Singleton.class);

        bind(Executor.class).annotatedWith(GlobalExecutor.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

}
