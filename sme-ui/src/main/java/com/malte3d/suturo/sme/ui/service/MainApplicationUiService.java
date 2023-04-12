package com.malte3d.suturo.sme.ui.service;

import com.malte3d.suturo.commons.javafx.FutureTaskBuilder;
import com.malte3d.suturo.commons.javafx.GlobalExecutor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.concurrent.Executor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MainApplicationUiService {

    @NonNull
    @GlobalExecutor
    private final Executor executor;

    private <T> FutureTaskBuilder<T> createFutureTask() {
        return new FutureTaskBuilder<>(executor);
    }

}
