package com.malte3d.suturo.sme.domain.model.semanticmap;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.NonNull;
import lombok.Value;

@ValueObject

@Value(staticConstructor = "of")
public class SemanticMapIdentifier {
    @NonNull
    String value;
}
