package com.malte3d.suturo.sme.adapter.persistence.application.settings;

import com.malte3d.suturo.sme.domain.model.application.exception.SmeException;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public class SettingsRepositoryImpl implements SettingsRepository {

    private static final String SETTINGS_PATH = Path.of("semantic-map-editor.properties").toAbsolutePath().toString();

    @Override
    public Settings load() {

        try (FileInputStream fileInputStream = new FileInputStream(SETTINGS_PATH)) {

            Properties properties = new Properties();
            properties.load(fileInputStream);

            return SettingsConverter.fromProperties(properties);

        } catch (FileNotFoundException e) {

            log.info("Settings not found in \"{}\". Creating default settings.", SETTINGS_PATH);

            save(Settings.DEFAULT);

        } catch (IOException e) {
            log.error("Error loading the settings", e);
            throw SmeException.of("Application.Settings.Load.Error", e);
        }

        return Settings.DEFAULT;
    }

    @Override
    public void save(@NonNull Settings settings) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(SETTINGS_PATH)) {

            Properties properties = SettingsConverter.toProperties(settings);
            properties.store(fileOutputStream, "Semantic Map Editor Settings");

            log.info("Settings saved in \"{}\"", SETTINGS_PATH);

        } catch (IOException e) {
            log.error("Error saving the settings", e);
            throw SmeException.of("Application.Settings.Save.Error", e);
        }
    }
}
