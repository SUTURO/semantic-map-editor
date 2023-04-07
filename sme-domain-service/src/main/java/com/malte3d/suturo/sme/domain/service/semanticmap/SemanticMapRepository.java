package com.malte3d.suturo.sme.domain.service.semanticmap;

import com.malte3d.suturo.commons.ddd.annotation.Repository;
import com.malte3d.suturo.sme.domain.model.semanticmap.SemanticMap;
import com.malte3d.suturo.sme.domain.model.semanticmap.SemanticMapIdentifier;
import lombok.NonNull;

import java.util.Optional;

@Repository
public interface SemanticMapRepository {

    Optional<SemanticMap> findById(@NonNull SemanticMapIdentifier identifier);

    void save(@NonNull SemanticMap semanticMap);

    void delete(@NonNull SemanticMap semanticMap);

}
