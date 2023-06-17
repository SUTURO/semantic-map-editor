package com.malte3d.suturo.sme.ui.view.scenegraph;

import javax.inject.Inject;

import com.jme3.scene.Spatial;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.ui.viewmodel.editor.EditorViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.ObjectAttachedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.EditorInitializedEvent;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScenegraphView {

    @Inject
    private EditorViewModel editorViewModel;

    @Inject
    private DomainEventHandler domainEventHandler;

    @FXML
    private VBox view;

    @FXML
    private TreeTableView<Spatial> scenegraphTable;

    @FXML
    private TreeTableColumn<Spatial, String> objectColumn;

    @FXML
    private TreeTableColumn<Spatial, String> visibilityColumn;

    TreeItem<Spatial> root;

    public void initialize() {

        domainEventHandler.register(EditorInitializedEvent.class, this::onEditorInitialized);
        domainEventHandler.register(ObjectAttachedEvent.class, this::onObjectAttached);

        scenegraphTable.prefHeightProperty().bind(view.heightProperty());
        scenegraphTable.prefWidthProperty().bind(view.widthProperty());
        scenegraphTable.setShowRoot(false);
        scenegraphTable.setEditable(true);

        scenegraphTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        scenegraphTable.getSelectionModel().setCellSelectionEnabled(true);

        objectColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getName()));
        objectColumn.prefWidthProperty().bind(view.widthProperty().multiply(0.7));

        visibilityColumn.prefWidthProperty().bind(view.widthProperty().multiply(0.25));
    }

    private void onEditorInitialized(EditorInitializedEvent event) {

        root = new ScenegraphNode(editorViewModel.getScenegraph());
        root.setExpanded(true);
        scenegraphTable.setRoot(root);
    }

    private void onObjectAttached(ObjectAttachedEvent event) {

        if (root != null)
            root.setValue(event.getObject());
    }

}
