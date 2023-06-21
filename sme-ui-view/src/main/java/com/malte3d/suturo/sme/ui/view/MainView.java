package com.malte3d.suturo.sme.ui.view;

import com.jayfella.jfx.embedded.jfx.EditorFxImageView;
import com.malte3d.suturo.commons.Version;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.commons.javafx.fxml.FxmlViewFactory;
import com.malte3d.suturo.commons.javafx.notification.NotificationHandler;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general.NullObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Plane;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Sphere;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import com.malte3d.suturo.sme.ui.view.scenegraph.ScenegraphView;
import com.malte3d.suturo.sme.ui.view.settings.SettingsView;
import com.malte3d.suturo.sme.ui.viewmodel.MainViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.editor.EditorViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.EditorInitializedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.TransformModeChangedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.transform.TransformMode;
import javafx.application.Platform;
import javafx.css.PseudoClass;
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

    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    @Inject
    private DomainEventHandler domainEventHandler;

    @Inject
    private MainViewModel mainViewModel;

    @Inject
    private EditorViewModel editorViewModel;

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

        domainEventHandler.register(TransformModeChangedEvent.class, event -> Platform.runLater(() -> onTransformModeChanged(event)));

        btnMove.setGraphic(new ImageView(Icons.TRANSFORM_MOVE));
        btnMove.setOnAction(event -> editorViewModel.setTransformMode(TransformMode.MOVE));
        btnMove.pseudoClassStateChanged(SELECTED, true);

        btnRotate.setGraphic(new ImageView(Icons.TRANSFORM_ROTATE));
        btnRotate.setOnAction(event -> editorViewModel.setTransformMode(TransformMode.ROTATE));

        btnScale.setGraphic(new ImageView(Icons.TRANSFORM_SCALE));
        btnScale.setOnAction(event -> editorViewModel.setTransformMode(TransformMode.SCALE));

        btnNull.setGraphic(new ImageView(Icons.OBJECT_NULL));
        btnNull.setOnAction(event -> editorViewModel.addObjectToScene(NullObject.DEFAULT));

        btnBox.setGraphic(new ImageView(Icons.OBJECT_BOX));
        btnBox.setOnAction(event -> editorViewModel.addObjectToScene(Box.DEFAULT));

        btnSphere.setGraphic(new ImageView(Icons.OBJECT_SPHERE));
        btnSphere.setOnAction(event -> editorViewModel.addObjectToScene(Sphere.DEFAULT));

        btnCylinder.setGraphic(new ImageView(Icons.OBJECT_CYLINDER));
        btnCylinder.setOnAction(event -> editorViewModel.addObjectToScene(Cylinder.DEFAULT));

        btnPlane.setGraphic(new ImageView(Icons.OBJECT_PLANE));
        btnPlane.setOnAction(event -> editorViewModel.addObjectToScene(Plane.DEFAULT));
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
        menuFileExit.setOnAction(event -> mainViewModel.exitApplication());

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
                mainViewModel.toggleDebugMode();
        });

       domainEventHandler.register(EditorInitializedEvent.class, () -> editorViewProgress.setVisible(false));

        editorViewModel.loadEditor().thenConsume(editor -> {

            EditorFxImageView editorImageView = editor.getImageView();

            editorView.getChildren().add(editorImageView);
            domainEventHandler.raise(new EditorInitializedEvent());
        });
    }

    private void onTransformModeChanged(TransformModeChangedEvent event) {

        TransformMode transformMode = event.getTransformMode();

        btnMove.pseudoClassStateChanged(SELECTED, transformMode == TransformMode.MOVE);
        btnRotate.pseudoClassStateChanged(SELECTED, transformMode == TransformMode.ROTATE);
        btnScale.pseudoClassStateChanged(SELECTED, transformMode == TransformMode.SCALE);
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
        copyrightOwnerLink.setOnAction(event -> mainViewModel.openCopyrightOwnerUrl());

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
