package com.malte3d.suturo.sme.ui.view.scenegraph;

import java.util.List;

import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.Node;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Plane;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Sphere;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

public class ScenegraphNode extends TreeItem<Node<SmObject>> {

    private static final int ICON_SIZE = 16;

    private boolean isFirstTimeChildren = true;

    public ScenegraphNode(Node<SmObject> value) {

        super(value);

        ImageView icon = getIcon(value.getData());
        icon.setFitHeight(ICON_SIZE);
        icon.setFitWidth(ICON_SIZE);

        setGraphic(icon);
    }

    @Override
    public ObservableList<TreeItem<Node<SmObject>>> getChildren() {

        if (isFirstTimeChildren) {

            isFirstTimeChildren = false;

            if (!getValue().isLeaf()) {

                List<ScenegraphNode> children = getValue().getChildren().stream()
                        .map(ScenegraphNode::new)
                        .toList();

                super.getChildren().setAll(children);
            }
        }

        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        return getValue().isLeaf();
    }

    /**
     * Returns the respective icon for the given object.
     *
     * @param object the object to get the icon for
     * @return the appropriate icon for the given object
     */
    private ImageView getIcon(SmObject object) {

        if (object instanceof Box)
            return new ImageView(Icons.TOOLBAR_BOX);
        else if (object instanceof Cylinder)
            return new ImageView(Icons.TOOLBAR_CYLINDER);
        else if (object instanceof Sphere)
            return new ImageView(Icons.TOOLBAR_SPHERE);
        else if (object instanceof Plane)
            return new ImageView(Icons.TOOLBAR_PLANE);

        return new ImageView(Icons.TOOLBAR_NULL);
    }
}
