package com.malte3d.suturo.sme.ui.view.settings;

import com.malte3d.suturo.sme.ui.viewmodel.settings.SettingsViewModel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SettingsView {

    private final SettingsViewModel viewModel;

    @FXML
    Parent view;

    public void initialize() {
        log.info("Initializing SettingsView");
    }

}
