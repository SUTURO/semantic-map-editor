package com.malte3d.suturo.sme.ui.service;

import com.malte3d.suturo.commons.javafx.CompletableFutureTaskBuilder;
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

    private <T> CompletableFutureTaskBuilder<T> createFutureTask() {
        return new CompletableFutureTaskBuilder<>(executor);
    }

}
