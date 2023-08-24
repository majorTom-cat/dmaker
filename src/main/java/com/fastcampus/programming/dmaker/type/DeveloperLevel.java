package com.fastcampus.programming.dmaker.type;

import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {

//    NEW("new developer", 0, 0),
//    JUNIOR("junior developer", 1, MAX_JUNIOR_EXPERIENCE_YEARS),
//    JUNGNIOR("jungnior developer", MAX_JUNIOR_EXPERIENCE_YEARS + 1, MIN_SENIOR_EXPERIENCE_YEARS - 1),
//    SENIOR("senior developer", MIN_SENIOR_EXPERIENCE_YEARS, 70)
//    ;

    NEW("new developer", years -> years == 0),
    JUNIOR("junior developer", years -> years <= MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("jungnior developer", years -> years > MAX_JUNIOR_EXPERIENCE_YEARS
        && years < MIN_SENIOR_EXPERIENCE_YEARS),
    SENIOR("senior developer", years -> years >= MIN_SENIOR_EXPERIENCE_YEARS);


    private final String description;
//    private final Integer minExperienceYears;
//    private final Integer maxExperienceYears;
    private final Function<Integer, Boolean> validateFunction;

    public void validateExperienceYears(Integer years) {
        if (!validateFunction.apply(years)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}

