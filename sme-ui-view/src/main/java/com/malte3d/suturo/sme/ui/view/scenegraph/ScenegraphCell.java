package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Plane;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Sphere;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;

public class ScenegraphCell extends TreeCell<SmObject> {

    private static final int ICON_SIZE = 16;

    @Override
    public void updateItem(SmObject object, boolean empty) {

        super.updateItem(object, empty);

        if (empty || object == null) {

            setText(null);
            setGraphic(null);
            setEditable(false);

        } else {

            ImageView icon = getIcon(object);
            icon.setFitHeight(ICON_SIZE);
            icon.setFitWidth(ICON_SIZE);

            setText(object.getName().getValue());
            setGraphic(icon);

        }
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
