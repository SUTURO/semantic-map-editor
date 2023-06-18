package com.malte3d.suturo.sme.ui.view.scenegraph;

import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.messages.Messages;
import javafx.css.PseudoClass;
import lombok.NonNull;

public enum DragTargetState {

    THIS("drag-target"),
    PREVIOUS("drag-target-previous"),
    NEXT("drag-target-next");

    public final PseudoClass pseudoClass;

    DragTargetState(@NonNull String pseudoClassName) {
        this.pseudoClass = PseudoClass.getPseudoClass(pseudoClassName);
    }

    public static DragTargetState of(@NonNull String pseudoClassName) {

        for (DragTargetState state : values()) {

            if (state.pseudoClass.getPseudoClassName().equals(pseudoClassName))
                return state;
        }

        throw new UnsupportedEnumException(Messages.format("{} for {} does not exist!", DragTargetState.class.getSimpleName(), pseudoClassName));
    }

}
