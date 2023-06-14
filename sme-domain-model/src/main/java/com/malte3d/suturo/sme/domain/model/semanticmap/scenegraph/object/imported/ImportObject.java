package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * An imported object.
 */
@ValueObject

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString
public class ImportObject extends SmObject {

    @NonNull
    ImportObjectType importType;

    @NonNull
    ImportObjectPath path;

    public ImportObject(@NonNull SmObjectName name, @NonNull Position position, @NonNull Rotation rotation, @NonNull ImportObjectType importType, @NonNull ImportObjectPath path) {
        super(name, SmObjectType.IMPORT, position, rotation);
        this.importType = importType;
        this.path = path;
    }
}
