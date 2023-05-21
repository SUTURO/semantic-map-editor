package com.malte3d.suturo.sme.ui.view.settings;

import com.malte3d.suturo.commons.javafx.fxml.EnumConverter;
import com.malte3d.suturo.commons.messages.Language;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.AdvancedSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.appearance.AppearanceSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.KeymapSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviour;
import com.malte3d.suturo.sme.ui.viewmodel.settings.SettingsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class SettingsView {

    @Inject
    private SettingsViewModel viewModel;

    @FXML
    private DialogPane view;

    /* Appearance */

    @FXML
    private ComboBox<Language> languageComboBox;

    /* Keymap */

    @FXML
    public ComboBox<CameraBehaviour> cameraBehaviourComboBox;

    /* Advanced */

    @FXML
    private CheckBox debugModeCheckBox;

    public void initialize() {

        view.getButtonTypes().addAll(ButtonType.CLOSE);

        initializeAppearance();
        initializeKeymap();
        initializeAdvanced();
    }

    private void initializeAppearance() {

        AppearanceSettings appearanceSettings = viewModel.getSettingsProperty().get().getAppearance();

        languageComboBox.getItems().addAll(Language.values());
        languageComboBox.setConverter(new EnumConverter<>());
        languageComboBox.getSelectionModel().select(appearanceSettings.getLanguage());
        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> saveSettings());
    }

    private void initializeKeymap() {

        KeymapSettings keymapSettings = viewModel.getSettingsProperty().get().getKeymap();

        CameraBehaviour cameraBehaviour = keymapSettings.getCameraBehaviour();
        cameraBehaviourComboBox.getItems().addAll(CameraBehaviour.values());
        cameraBehaviourComboBox.setConverter(new EnumConverter<>());
        cameraBehaviourComboBox.getSelectionModel().select(cameraBehaviour);
        cameraBehaviourComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> saveSettings());
    }

    private void initializeAdvanced() {

        AdvancedSettings advancedSettings = viewModel.getSettingsProperty().get().getAdvanced();

        DebugMode debugMode = advancedSettings.getDebugMode();
        debugModeCheckBox.setSelected(debugMode.isEnabled());
        debugModeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> saveSettings());
    }

    public void saveSettings() {
        viewModel.saveSettings(getCurrentSettings());
    }

    private Settings getCurrentSettings() {

        AppearanceSettings appearanceSettings = new AppearanceSettings(
                languageComboBox.getSelectionModel().getSelectedItem()
        );

        KeymapSettings keymapSettings = new KeymapSettings(
                cameraBehaviourComboBox.getSelectionModel().getSelectedItem()
        );

        AdvancedSettings advancedSettings = new AdvancedSettings(
                DebugMode.of(debugModeCheckBox.isSelected())
        );

        return new Settings(
                appearanceSettings,
                keymapSettings,
                advancedSettings
        );
    }

}
