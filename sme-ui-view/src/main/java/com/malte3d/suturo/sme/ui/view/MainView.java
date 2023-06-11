package com.malte3d.suturo.sme.ui.view;

import com.jayfella.jfx.embedded.jfx.EditorFxImageView;
import com.malte3d.suturo.commons.Version;
import com.malte3d.suturo.commons.javafx.fxml.FxmlViewFactory;
import com.malte3d.suturo.commons.javafx.notification.NotificationHandler;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import com.malte3d.suturo.sme.ui.view.scenegraph.ScenegraphView;
import com.malte3d.suturo.sme.ui.view.settings.SettingsView;
import com.malte3d.suturo.sme.ui.viewmodel.MainViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.EditorInitializedEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.Year;


@Slf4j
public class MainView {

    @Inject
    private MainViewModel viewModel;

    @Inject
    private FxmlViewFactory viewFactory;

    @FXML
    private Parent view;

    /*
     * Menu
     */

    @FXML
    private MenuBar menuBar;

    /* File */
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem menuFileNew;
    @FXML
    private MenuItem menuFileOpen;
    @FXML
    private Menu menuFileOpenRecent;
    @FXML
    private MenuItem menuFileClose;
    @FXML
    private MenuItem menuFileImport;
    @FXML
    private MenuItem menuFileSave;
    @FXML
    private MenuItem menuFileExport;
    @FXML
    private MenuItem menuFileSettings;
    @FXML
    private MenuItem menuFileExit;

    /* Edit */
    @FXML
    private Menu menuEdit;
    @FXML
    private MenuItem menuEditUndo;
    @FXML
    private MenuItem menuEditRedo;
    @FXML
    private MenuItem menuEditCut;
    @FXML
    private MenuItem menuEditCopy;
    @FXML
    private MenuItem menuEditPaste;
    @FXML
    private MenuItem menuEditDelete;

    /* Help */
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuHelpAbout;

    /*
     * Toolbar
     */
    @FXML
    private Button btnMove;
    @FXML
    private Button btnRotate;
    @FXML
    private Button btnScale;
    @FXML
    private Button btnNull;
    @FXML
    private Button btnBox;
    @FXML
    private Button btnSphere;
    @FXML
    private Button btnCylinder;
    @FXML
    private Button btnPlane;

    /*
     * Editor View
     */

    @FXML
    private StackPane editorView;
    @FXML
    private ProgressIndicator editorViewProgress;

    /*
     * Scenegraph & Properties View
     */

    @FXML
    private StackPane scenegraphView;

    @FXML
    private AnchorPane propertiesView;


    /*
     * Statusbar
     */

    @FXML
    private HBox statusbar;

    public void initialize() {

        initMenuView();
        initToolbarView();
        initEditorView();

        scenegraphView.getChildren().add(viewFactory.loadView(ScenegraphView.class));
    }

    private void initToolbarView() {

        btnMove.setGraphic(new ImageView(Icons.TOOLBAR_MOVE));
        btnRotate.setGraphic(new ImageView(Icons.TOOLBAR_ROTATE));
        btnScale.setGraphic(new ImageView(Icons.TOOLBAR_SCALE));

        btnNull.setGraphic(new ImageView(Icons.TOOLBAR_NULL));
        btnBox.setGraphic(new ImageView(Icons.TOOLBAR_BOX));
        btnSphere.setGraphic(new ImageView(Icons.TOOLBAR_SPHERE));
        btnCylinder.setGraphic(new ImageView(Icons.TOOLBAR_CYLINDER));
        btnPlane.setGraphic(new ImageView(Icons.TOOLBAR_PLANE));

    }

    private void initMenuView() {
        initMenuFile();
        initMenuEdit();
        initMenuHelp();
    }

    private void initMenuFile() {

        menuFileNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        menuFileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        menuFileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        menuFileExport.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        menuFileSettings.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.ALT_DOWN));

        menuFileClose.setOnAction(event -> showHelloWorldNotification());

        menuFileSettings.setOnAction(event -> openSettings());
        menuFileExit.setOnAction(event -> viewModel.exitApplication());

        /* TODO: Actual implementation for file menu  */
        menuFileNew.setDisable(true);
        menuFileOpen.setDisable(true);
        menuFileOpenRecent.setDisable(true);
        menuFileImport.setDisable(true);
        menuFileSave.setDisable(true);
        menuFileExport.setDisable(true);
    }

    private void initMenuEdit() {

        menuEditUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        menuEditRedo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        menuEditCut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        menuEditCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        menuEditPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        menuEditDelete.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));

        /* TODO: Actual implementation for edit menu  */
        menuEditUndo.setDisable(true);
        menuEditRedo.setDisable(true);
        menuEditCut.setDisable(true);
        menuEditCopy.setDisable(true);
        menuEditPaste.setDisable(true);
        menuEditDelete.setDisable(true);
    }

    private void initMenuHelp() {

        menuHelpAbout.setOnAction(event -> openHelpAboutDialog());
    }

    private void initEditorView() {

        view.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.F3)
                viewModel.toggleDebugMode();
        });

        viewModel.getDomainEventHandler().register(EditorInitializedEvent.class, () -> editorViewProgress.setVisible(false));

        viewModel.loadEditor().thenConsume(editor -> {

            EditorFxImageView editorImageView = editor.getImageView();

            editorView.getChildren().add(editorImageView);
            viewModel.getDomainEventHandler().raise(new EditorInitializedEvent());
        });
    }

    private void showHelloWorldNotification() {

        NotificationHandler.create()
                .title(Messages.getString("Application.Notification.Title.Info"))
                .text("Hello World!")
                .showInformation();
    }

    private void openSettings() {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(Messages.getString("Application.Settings.Title"));
        alert.setDialogPane(viewFactory.loadView(SettingsView.class));
        alert.initOwner(getMainWindow());
        alert.showAndWait();
    }

    private void openHelpAboutDialog() {

        Hyperlink copyrightOwnerLink = new Hyperlink(Messages.getString("Application.Help.About.CopyrightOwner"));
        copyrightOwnerLink.setOnAction(event -> viewModel.openCopyrightOwnerUrl());

        TextFlow copyrightText = new TextFlow(
                new Text(Messages.getString("Application.Help.About.Copyright", Year.now().getValue())),
                new Text(" "),
                copyrightOwnerLink
        );

        ButtonType closeButton = new ButtonType(Messages.getString("Application.Help.About.Button.Close"), ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Messages.getString("Application.Help.About.Title"));
        alert.setHeaderText(Messages.getString("Application.Help.About.Header", Version.getVersion()));
        alert.getDialogPane().setContent(copyrightText);
        alert.getDialogPane().getButtonTypes().setAll(closeButton);
        alert.initOwner(getMainWindow());
        alert.showAndWait();
    }

    private Window getMainWindow() {
        return view.getScene().getWindow();
    }
}
