package com.malte3d.suturo.sme.domain.model.semanticmap;

import com.malte3d.suturo.commons.ddd.annotation.AggregateRoot;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@AggregateRoot

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SemanticMap {

    @NonNull
    @EqualsAndHashCode.Include
    SemanticMapIdentifier id;

    @NonNull
    SemanticMapName name;

}
