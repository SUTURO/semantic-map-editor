package com.malte3d.suturo.sme.domain.service.application.settings;

import com.malte3d.suturo.commons.ddd.annotation.Repository;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import lombok.NonNull;

@Repository
public interface SettingsRepository {

    Settings load();

    void save(@NonNull Settings settings);

}
