package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.jme3.scene.Spatial;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.ui.viewmodel.editor.EditorViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.ObjectAttachedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.EditorInitializedEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Optional;

@Slf4j
public class ScenegraphView {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

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

    private TreeItem<Spatial> root;

    private final Timeline scrolltimeline = new Timeline();
    private double scrollDirection = 0;

    public void initialize() {

        domainEventHandler.register(EditorInitializedEvent.class, this::onEditorInitialized);
        domainEventHandler.register(ObjectAttachedEvent.class, this::onObjectAttached);

        scenegraphTable.prefHeightProperty().bind(view.heightProperty());
        scenegraphTable.prefWidthProperty().bind(view.widthProperty());
        scenegraphTable.setShowRoot(false);
        scenegraphTable.setEditable(true);
        scenegraphTable.setRowFactory(this::rowFactory);

        scenegraphTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        scenegraphTable.getSelectionModel().setCellSelectionEnabled(true);

        objectColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        objectColumn.prefWidthProperty().bind(view.widthProperty().multiply(0.7));

        visibilityColumn.prefWidthProperty().bind(view.widthProperty().multiply(0.25));

        setupScrolling();
    }

    private void onEditorInitialized(EditorInitializedEvent event) {

        root = new TreeItem<>(editorViewModel.getScenegraph());
        root.setExpanded(true);
        scenegraphTable.setRoot(root);
    }

    private void onObjectAttached(ObjectAttachedEvent event) {

        if (root != null) {

            TreeItem<Spatial> item = new TreeItem<>(event.getObject());
            item.setExpanded(true);

            root.getChildren().add(item);
        }
    }

    private TreeTableRow<Spatial> rowFactory(TreeTableView<Spatial> view) {

        TreeTableRow<Spatial> row = new TreeTableRow<>();

        row.setOnDragDetected(event -> {

            if (!row.isEmpty()) {

                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(row.snapshot(null, null));
                ClipboardContent cc = new ClipboardContent();
                cc.put(SERIALIZED_MIME_TYPE, row.getIndex());
                db.setContent(cc);
                event.consume();
            }
        });

        row.setOnDragOver(event -> {

            Dragboard db = event.getDragboard();

            if (acceptable(db, row)) {

                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });

        row.setOnDragDropped(event -> {

            Dragboard db = event.getDragboard();

            if (acceptable(db, row)) {

                int rowIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                TreeItem<Spatial> item = scenegraphTable.getTreeItem(rowIndex);
                item.getParent().getChildren().remove(item);
                getDragTarget(row).getChildren().add(item);

                scenegraphTable.getSelectionModel().clearSelection();
                scenegraphTable.getSelectionModel().select(item);
                scenegraphTable.refresh();
                
                event.setDropCompleted(true);
                event.consume();
            }
        });

        return row;
    }

    /**
     * Checks if the row is acceptable to be dragged to
     *
     * @param db  The dragboard
     * @param row The row
     * @return true, if the row is acceptable to be dragged to
     */
    private boolean acceptable(Dragboard db, TreeTableRow<Spatial> row) {

        boolean result = false;

        if (db.hasContent(SERIALIZED_MIME_TYPE)) {

            int index = (Integer) db.getContent(SERIALIZED_MIME_TYPE);

            if (row.getIndex() != index) {

                TreeItem<Spatial> target = getDragTarget(row);
                TreeItem<Spatial> item = scenegraphTable.getTreeItem(index);
                result = !isParent(item, target);
            }
        }

        return result;
    }

    /**
     * Gets the drag target for the given row
     *
     * @param row The row
     * @return The drag target for the row
     */
    private TreeItem<Spatial> getDragTarget(TreeTableRow<Spatial> row) {

        if (row.isEmpty())
            return scenegraphTable.getRoot();

        return row.getTreeItem();
    }

    /**
     * Checks if parent is a parent of child
     *
     * @param parent The potential parent node
     * @param child  The child node
     * @return true, if parent is a parent of child
     */
    private boolean isParent(TreeItem<Spatial> parent, TreeItem<Spatial> child) {

        boolean result = false;

        while (!result && child != null) {
            result = child.getParent() == parent;
            child = child.getParent();
        }

        return result;
    }

    private void setupScrolling() {

        scrolltimeline.setCycleCount(Timeline.INDEFINITE);
        scrolltimeline.getKeyFrames().add(new KeyFrame(Duration.millis(20), "DragScroll", (ActionEvent) -> dragScroll()));

        scenegraphTable.setOnDragExited(event -> {

            if (event.getY() > 0)
                scrollDirection = 1.0 / scenegraphTable.getExpandedItemCount();
            else
                scrollDirection = -1.0 / scenegraphTable.getExpandedItemCount();

            scrolltimeline.play();
        });

        scenegraphTable.setOnDragEntered(event -> scrolltimeline.stop());
        scenegraphTable.setOnDragDone(event -> scrolltimeline.stop());
    }

    private void dragScroll() {

        getVerticalScrollbar().ifPresent(scrollBar -> {
            double newValue = scrollBar.getValue() + scrollDirection;
            scrollBar.setValue(Math.max(0.0, Math.min(newValue, 1.0)));
        });
    }

    private Optional<ScrollBar> getVerticalScrollbar() {

        for (Node node : scenegraphTable.lookupAll(".scroll-bar")) {

            if (node instanceof ScrollBar scrollBar)
                if (scrollBar.getOrientation().equals(Orientation.VERTICAL))
                    return Optional.of(scrollBar);
        }

        return Optional.empty();
    }

}
