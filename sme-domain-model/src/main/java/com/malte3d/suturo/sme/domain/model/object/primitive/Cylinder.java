package com.malte3d.suturo.sme.domain.model.object.primitive;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.Value;

@ValueObject

@Value
public class Cylinder implements Primitive {

    double height;
    double radius;
}
