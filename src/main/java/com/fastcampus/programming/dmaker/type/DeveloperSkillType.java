package com.fastcampus.programming.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeveloperSkillType {

    BACK_END("back-end developer"),
    FRONT_END("front-end developer"),
    FULL_STACK("full-stack developer");

    private final String description;

}
