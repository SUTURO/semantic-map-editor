package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.*;
import lombok.*;
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

    public ImportObject(@NonNull SmObjectName name, @NonNull ImportObjectType importType, @NonNull ImportObjectPath path) {
        super(name, SmObjectType.IMPORT, Position.ZERO, Rotation.IDENTITY);
        this.importType = importType;
        this.path = path;
    }

    public ImportObject(@NonNull SmObjectName name, @NonNull Position position, @NonNull Rotation rotation, @NonNull ImportObjectType importType, @NonNull ImportObjectPath path) {
        super(name, SmObjectType.IMPORT, position, rotation);
        this.importType = importType;
        this.path = path;
    }
}
