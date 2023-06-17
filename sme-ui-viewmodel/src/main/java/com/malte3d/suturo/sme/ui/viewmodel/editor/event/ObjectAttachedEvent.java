package com.malte3d.suturo.sme.ui.viewmodel.editor.event;

import com.jme3.scene.Spatial;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Event that is raised when an object has been attached to the scene graph.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ObjectAttachedEvent extends DomainEvent {

    Spatial object;
    
}
