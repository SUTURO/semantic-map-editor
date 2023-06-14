package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.Node;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general.NullObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder;
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

    private static final Node<SmObject> ROOT = new Node<>(NullObject.ROOT);

    @FXML
    private VBox view;

    @FXML
    private TreeTableView<Node<SmObject>> scenegraphTable;

    @FXML
    private TreeTableColumn<Node<SmObject>, String> objectColumn;

    @FXML
    private TreeTableColumn<Node<SmObject>, String> visibilityColumn;

    public void initialize() {

        initTree();

        TreeItem<Node<SmObject>> root = new ScenegraphNode(ROOT);
        root.setExpanded(true);

        scenegraphTable.prefHeightProperty().bind(view.heightProperty());
        scenegraphTable.prefWidthProperty().bind(view.widthProperty());
        scenegraphTable.setRoot(root);
        scenegraphTable.setShowRoot(false);
        scenegraphTable.setEditable(true);

        scenegraphTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        scenegraphTable.getSelectionModel().setCellSelectionEnabled(true);

        objectColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getData().getName().getValue()));
        objectColumn.prefWidthProperty().bind(view.widthProperty().multiply(0.7));

        visibilityColumn.prefWidthProperty().bind(view.widthProperty().multiply(0.25));
    }

    private void initTree() {

        Node<SmObject> child1 = new Node<>(NullObject.DEFAULT);
        Node<SmObject> child2 = new Node<>(Cylinder.DEFAULT);
        Node<SmObject> child12 = new Node<>(Box.DEFAULT);

        child1.addChild(child12);
        ROOT.addChild(child1);
        ROOT.addChild(child2);
    }
}
