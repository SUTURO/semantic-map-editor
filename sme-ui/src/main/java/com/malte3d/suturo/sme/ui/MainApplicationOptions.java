package com.malte3d.suturo.sme.ui;

import com.google.inject.Injector;
import com.malte3d.suturo.commons.ddd.annotation.DPO;
import lombok.NonNull;
import lombok.Value;

@DPO

@Value
public class MainApplicationOptions {

    @NonNull
    Injector injector;

    @NonNull
    String[] args;

}
