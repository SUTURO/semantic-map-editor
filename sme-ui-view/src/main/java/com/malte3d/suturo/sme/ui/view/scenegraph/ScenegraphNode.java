package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import com.malte3d.suturo.sme.ui.viewmodel.editor.Editor;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.util.List;

public class ScenegraphNode extends TreeItem<Spatial> {

    private static final int ICON_SIZE = 16;

    private boolean isFirstTimeChildren = true;

    public ScenegraphNode(Spatial value) {

        super(value);

        SmObjectType objectType = SmObjectType.of(value.getUserData(Editor.OBJECT_TYPE));
        ImageView icon = getIcon(objectType);
        icon.setFitHeight(ICON_SIZE);
        icon.setFitWidth(ICON_SIZE);
        setExpanded(true);

        setGraphic(icon);
    }

    @Override
    public ObservableList<TreeItem<Spatial>> getChildren() {

        if (isFirstTimeChildren) {

            isFirstTimeChildren = false;

            if (!isLeaf()) {

                List<ScenegraphNode> children = ((Node) getValue()).getChildren().stream()
                        .map(ScenegraphNode::new)
                        .toList();

                super.getChildren().setAll(children);
            }
        }

        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        return !(getValue() instanceof Node node) || node.getChildren().isEmpty();
    }

    /**
     * Returns the respective icon for the given object type.
     *
     * @param type the object type to get the icon for
     * @return the appropriate icon for the given object
     */
    private ImageView getIcon(SmObjectType type) {

        return switch (type) {
            case BOX -> new ImageView(Icons.TOOLBAR_BOX);
            case CYLINDER -> new ImageView(Icons.TOOLBAR_CYLINDER);
            case SPHERE -> new ImageView(Icons.TOOLBAR_SPHERE);
            case PLANE -> new ImageView(Icons.TOOLBAR_PLANE);
            default -> new ImageView(Icons.TOOLBAR_NULL);
        };
    }
}
