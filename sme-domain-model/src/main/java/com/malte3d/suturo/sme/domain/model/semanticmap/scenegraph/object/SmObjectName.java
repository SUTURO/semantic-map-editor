package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.NonNull;
import lombok.Value;

@ValueObject

@Value(staticConstructor = "of")
public class SmObjectName {
    @NonNull
    String value;
}
