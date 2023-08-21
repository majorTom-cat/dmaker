package com.fastcampus.programming.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {

    NEW("new developer"),
    JUNIOR("junior developer"),
    JUNGNIOR("jungnior developer"),
    SENIOR("senior developer")
    ;

    private final String description;

}

