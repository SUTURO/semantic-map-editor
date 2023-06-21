package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.jme3.scene.Spatial;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import com.malte3d.suturo.sme.ui.viewmodel.editor.Editor;
import javafx.scene.control.TreeTableCell;
import javafx.scene.image.ImageView;

public class ScenegraphViewObjectPropertiesCell extends TreeTableCell<Spatial, Boolean> {

    private static final int ICON_SIZE = 16;

    @Override
    protected void updateItem(Boolean visible, boolean empty) {

        super.updateItem(visible, empty);

        if (empty || visible == null) {

            setGraphic(null);

        } else {

            ImageView icon = getIcon(visible);
            icon.setFitHeight(ICON_SIZE);
            icon.setFitWidth(ICON_SIZE);

            setGraphic(icon);
        }
    }

    /**
     * Returns the respective icon for the given property.
     *
     * @param visible wether the object is visible or not
     * @return the appropriate icon for the given visibility
     */
    private ImageView getIcon(boolean visible) {

        if (visible)
            return new ImageView(Icons.VISIBLE);
        else
            return new ImageView(Icons.INVISIBLE);
    }
}
