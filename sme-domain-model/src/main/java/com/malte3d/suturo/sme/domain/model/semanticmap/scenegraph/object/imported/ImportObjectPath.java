package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.NonNull;
import lombok.Value;

/**
 * The file path to the imported object.
 */
@ValueObject

@Value(staticConstructor = "of")
public class ImportObjectPath {
    @NonNull
    String value;
}
