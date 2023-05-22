package com.malte3d.suturo.sme.domain.model.semanticmap;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@ValueObject

@Value(staticConstructor = "of")
public class SemanticMapIdentifier {

    @NonNull
    String value;

    /**
     * Creates a new random semantic map identifier.
     *
     * @return a new random semantic map identifier
     */
    public static SemanticMapIdentifier create() {
        return SemanticMapIdentifier.of(UUID.randomUUID().toString());
    }
}
