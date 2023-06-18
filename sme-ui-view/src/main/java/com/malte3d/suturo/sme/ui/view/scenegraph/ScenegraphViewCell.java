package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.jme3.scene.Spatial;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import com.malte3d.suturo.sme.ui.viewmodel.editor.Editor;
import javafx.scene.control.TreeTableCell;
import javafx.scene.image.ImageView;

/**
 * Cell factory for the scenegraph table.
 *
 * <p>
 * Displays the name of the object and an icon representing the object type.
 * </p>
 */
public class ScenegraphViewCell extends TreeTableCell<Spatial, String> {

    private static final int ICON_SIZE = 16;

    @Override
    protected void updateItem(String name, boolean empty) {

        super.updateItem(name, empty);

        if (empty || name == null) {

            setText(null);
            setGraphic(null);

        } else {

            Spatial item = getTableRow().getTreeItem().getValue();
            SmObjectType objectType = SmObjectType.of(item.getUserData(Editor.OBJECT_TYPE));
            ImageView icon = getIcon(objectType);
            icon.setFitHeight(ICON_SIZE);
            icon.setFitWidth(ICON_SIZE);

            setText(name);
            setGraphic(icon);
        }
    }

    /**
     * Returns the respective icon for the given object type.
     *
     * @param type the object type to get the icon for
     * @return the appropriate icon for the given object
     */
    private ImageView getIcon(SmObjectType type) {

        return switch (type) {
            case BOX -> new ImageView(Icons.OBJECT_BOX);
            case CYLINDER -> new ImageView(Icons.OBJECT_CYLINDER);
            case SPHERE -> new ImageView(Icons.OBJECT_SPHERE);
            case PLANE -> new ImageView(Icons.OBJECT_PLANE);
            default -> new ImageView(Icons.OBJECT_NULL);
        };
    }
}
