package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.jme3.scene.Spatial;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general.NullObject;
import com.malte3d.suturo.sme.ui.viewmodel.editor.EditorViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.EditorInitializedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.ObjectAttachedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.ObjectSelectedEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Optional;

@Slf4j
public class ScenegraphView {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private static final double DRAG_TARGET_THRESHOLD = 0.85;

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

        domainEventHandler.register(EditorInitializedEvent.class, event -> Platform.runLater(() -> onEditorInitialized(event)));
        domainEventHandler.register(ObjectAttachedEvent.class, event -> Platform.runLater(() -> onObjectAttached(event)));
        domainEventHandler.register(ObjectSelectedEvent.class, event -> Platform.runLater(() -> onObjectSelected(event)));

        scenegraphTable.prefHeightProperty().bind(view.heightProperty());
        scenegraphTable.prefWidthProperty().bind(view.widthProperty());
        scenegraphTable.setShowRoot(false);
        scenegraphTable.setEditable(true);
        scenegraphTable.setRowFactory(this::rowFactory);

        scenegraphTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        scenegraphTable.getSelectionModel().setCellSelectionEnabled(true);
        scenegraphTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {

            if (newSelection != null)
                domainEventHandler.raise(new ObjectSelectedEvent(Optional.ofNullable(newSelection.getValue()), ObjectSelectedEvent.Origin.SCENEGRAPH_VIEW));
            else
                domainEventHandler.raise(new ObjectSelectedEvent(Optional.empty(), ObjectSelectedEvent.Origin.SCENEGRAPH_VIEW));
        });

        objectColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        objectColumn.setCellFactory(param -> new ScenegraphViewCell());

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

    private void onObjectSelected(ObjectSelectedEvent event) {

        if (ObjectSelectedEvent.Origin.EDITOR.equals(event.getOrigin())) {

            event.getSelectedObject().ifPresentOrElse(spatial -> {

                findTreeItem(root, spatial).ifPresent(item -> {

                    int row = scenegraphTable.getRow(item);
                    scenegraphTable.requestFocus();
                    scenegraphTable.getSelectionModel().select(row);
                    scenegraphTable.scrollTo(row);
                });

            }, () -> scenegraphTable.getSelectionModel().clearSelection());
        }
    }

    private TreeTableRow<Spatial> rowFactory(TreeTableView<Spatial> view) {

        TreeTableRow<Spatial> row = new TreeTableRow<>();

        row.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            row.updateSelected(true);
            log.info("Selected: {}", row);
        });

        row.setOnDragDetected(event -> {

            if (!row.isEmpty()) {

                ClipboardContent cc = new ClipboardContent();
                cc.put(SERIALIZED_MIME_TYPE, row.getIndex());

                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(row.snapshot(null, null));
                db.setContent(cc);

                event.consume();
            }
        });

        row.setOnDragExited(event -> {

            Dragboard db = event.getDragboard();

            if (acceptable(db, row, event)) {
                activateDragTargetState(row, null);
                event.consume();
            }
        });

        row.setOnDragOver(event -> {

            Dragboard db = event.getDragboard();

            if (acceptable(db, row, event)) {

                if (!row.isEmpty()) {

                    DragTarget<Spatial> target = getDragTarget(row, event);

                    if (target.isBetween()) {

                        if (target.isNext())
                            activateDragTargetState(row, DragTargetState.NEXT);
                        else
                            activateDragTargetState(row, DragTargetState.PREVIOUS);

                    } else {

                        activateDragTargetState(row, DragTargetState.THIS);
                    }
                }

                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();

            } else {

                activateDragTargetState(row, null);
            }
        });

        row.setOnDragDropped(event -> {

            Dragboard db = event.getDragboard();

            if (acceptable(db, row, event)) {

                int rowIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                TreeItem<Spatial> item = scenegraphTable.getTreeItem(rowIndex);
                item.getParent().getChildren().remove(item);
                DragTarget<Spatial> target = getDragTarget(row, event);
                target.getItem().getChildren().add(target.getIndex(), item);

                scenegraphTable.getSelectionModel().clearSelection();
                scenegraphTable.getSelectionModel().select(item);

                editorViewModel.setScenegraph(getCurrentView());
                scenegraphTable.refresh();

                event.setDropCompleted(true);
                event.consume();
            }
        });

        return row;
    }

    private void activateDragTargetState(TreeTableRow<Spatial> row, DragTargetState activeState) {

        for (DragTargetState state : DragTargetState.values()) {

            row.pseudoClassStateChanged(state.pseudoClass, false);

            if (activeState != null && state == activeState)
                row.pseudoClassStateChanged(state.pseudoClass, true);
        }
    }

    /**
     * Checks if the row is acceptable to be dragged to.
     *
     * <p>
     * Geometries can't be dragged as children to other geometries, only to nodes ({@link NullObject}s).
     * </p>
     *
     * @param db  The dragboard
     * @param row The row
     * @return true, if the row is acceptable to be dragged to
     */
    private boolean acceptable(Dragboard db, TreeTableRow<Spatial> row, DragEvent event) {

        if (row.isEmpty() || !db.hasContent(SERIALIZED_MIME_TYPE))
            return true;

        int index = (Integer) db.getContent(SERIALIZED_MIME_TYPE);

        if (row.getIndex() == index)
            return false;

        DragTarget<Spatial> target = getDragTarget(row, event);
        TreeItem<Spatial> item = scenegraphTable.getTreeItem(index);
        boolean isParent = isParent(item, target.getItem());

        return !isParent && (target.isBetween() || row.getItem() instanceof com.jme3.scene.Node);
    }

    /**
     * Gets the drag target for the given row
     *
     * @param row   The row
     * @param event The drag event
     * @return The drag target for the row
     */
    private DragTarget<Spatial> getDragTarget(TreeTableRow<Spatial> row, DragEvent event) {

        if (row.isEmpty())
            return new DragTarget<>(scenegraphTable.getRoot().getChildren().size(), scenegraphTable.getRoot());

        double height = row.getHeight();
        double localY = event.getY();

        double max = height * DRAG_TARGET_THRESHOLD;
        double min = height - max;

        if (localY < min)
            return new DragTarget<>(getRowIndexInParent(row), row.getTreeItem().getParent(), true, false);
        else if (localY > max)
            return new DragTarget<>(getRowIndexInParent(row) + 1, row.getTreeItem().getParent(), true, true);

        return new DragTarget<>(0, row.getTreeItem());
    }

    private int getRowIndexInParent(TreeTableRow<Spatial> row) {

        TreeItem<Spatial> treeItem = row.getTreeItem();
        TreeItem<Spatial> parent = treeItem.getParent();

        if (parent != null)
            return parent.getChildren().indexOf(treeItem);

        return 0;
    }

    /**
     * Checks if parent is a parent of child
     *
     * @param parent The potential parent node
     * @param child  The child node
     * @return true, if parent is a parent of child or if parent and child are equal
     */
    private boolean isParent(TreeItem<Spatial> parent, TreeItem<Spatial> child) {

        if (parent.equals(child))
            return true;

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

    /**
     * Finds the tree item for the given target node. Starts the search at the given root node.
     *
     * @param root   The root node
     * @param target The target node
     * @return The tree item for the target node or an empty optional if the target node is not in the tree
     */
    private Optional<TreeItem<Spatial>> findTreeItem(TreeItem<Spatial> root, Spatial target) {

        if (root.getValue().equals(target))
            return Optional.of(root);


        for (TreeItem<Spatial> child : root.getChildren()) {

            Optional<TreeItem<Spatial>> result = findTreeItem(child, target);

            if (result.isPresent())
                return result;
        }

        return Optional.empty();
    }

    private com.jme3.scene.Node getCurrentView() {
        return (com.jme3.scene.Node) treeToSpatial(root);
    }

    private Spatial treeToSpatial(TreeItem<Spatial> tree) {

        Spatial spatial = tree.getValue();

        if (spatial instanceof com.jme3.scene.Node node) {

            node.detachAllChildren();

            for (TreeItem<Spatial> child : tree.getChildren())
                node.attachChild(treeToSpatial(child));
        }

        return spatial;
    }
}
