package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general.NullObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScenegraphView {

    @FXML
    private VBox view;

    @FXML
    private TreeView<SmObject> scenegraph;

    public void initialize() {

        TreeItem<SmObject> root = new TreeItem<>(NullObject.DEFAULT);
        TreeItem<SmObject> child1 = new TreeItem<>(NullObject.DEFAULT);
        TreeItem<SmObject> child2 = new TreeItem<>(Cylinder.DEFAULT);
        TreeItem<SmObject> child12 = new TreeItem<>(Box.DEFAULT);

        child1.getChildren().add(child12);
        root.getChildren().addAll(child1, child2);

        scenegraph.prefHeightProperty().bind(view.heightProperty());
        scenegraph.prefWidthProperty().bind(view.widthProperty());
        scenegraph.setRoot(root);
        scenegraph.setEditable(true);
        scenegraph.setShowRoot(false);
        scenegraph.setCellFactory(param -> new ScenegraphCell());
        scenegraph.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
