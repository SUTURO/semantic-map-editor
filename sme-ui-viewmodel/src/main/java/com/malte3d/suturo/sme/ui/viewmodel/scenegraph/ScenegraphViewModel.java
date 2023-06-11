package com.malte3d.suturo.sme.ui.viewmodel.scenegraph;

import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.service.UiService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.concurrent.Executor;

@Slf4j
public class ScenegraphViewModel extends UiService {

    @Inject
    public ScenegraphViewModel(
            @NonNull @GlobalExecutor Executor executor
    ) {

        super(executor);

    }
}
