package com.malte3d.suturo.sme.ui.view;

import com.malte3d.suturo.sme.ui.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
public class MainView {

    private final MainViewModel viewModel;

    @FXML
    Parent mainView;

    public void initialize() {

        JMetro jMetro = new JMetro(mainView, Style.LIGHT);
        jMetro.getOverridingStylesheets().add(Objects.requireNonNull(MainView.class.getResource("sme-base.css")).toExternalForm());
    }
}
