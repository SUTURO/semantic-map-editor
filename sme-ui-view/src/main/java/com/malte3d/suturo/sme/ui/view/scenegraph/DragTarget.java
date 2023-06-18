package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import javafx.scene.control.TreeItem;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@ValueObject

@Value
@AllArgsConstructor
public class DragTarget<T> {

    int index;

    @NonNull
    TreeItem<T> item;

    /**
     * true, if the drag target is between two existing items
     */
    boolean between;

    /**
     * true, if the drag target is after the current row (between current row item and next item)
     */
    boolean next;

    public DragTarget(int index, @NonNull TreeItem<T> item) {
        this.index = index;
        this.item = item;
        this.between = false;
        this.next = false;
    }
}
