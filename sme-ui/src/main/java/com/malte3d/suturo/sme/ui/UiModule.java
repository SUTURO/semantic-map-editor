package com.malte3d.suturo.sme.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.malte3d.suturo.commons.javafx.fxml.FxmlViewFactory;
import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import com.malte3d.suturo.sme.ui.viewmodel.main.MainViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.settings.SettingsViewModel;
import javafx.application.HostServices;
import lombok.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(HostServices.class).toProvider(MainApplication.class).in(Singleton.class);
        bind(Executor.class).annotatedWith(GlobalExecutor.class).toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
    }

    @Singleton
    @Provides
    public FxmlViewFactory fxmlViewFactory(
            @NonNull MainViewModel mainViewModel,
            @NonNull SettingsViewModel settingsViewModel
    ) {
        return new MainApplicationViewFactory(mainViewModel, settingsViewModel);
    }

}
