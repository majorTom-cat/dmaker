package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.type.DeveloperLevel.*;
import static com.fastcampus.programming.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(FRONT_END)
            .experienceYears(12)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(12)
            .build();

    private CreateDeveloper.Request getCreateRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .memberId("memberId")
                .name("name")
                .age(32)
                .build();
    }

    @Test
    public void testSomething() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(Developer.builder()
                        .developerLevel(SENIOR)
                        .developerSkillType(FRONT_END)
                        .experienceYears(12)
                        .statusCode(StatusCode.EMPLOYED)
                        .name("name")
                        .age(12)
                        .build()));

        //when
        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        //then
        assertEquals(SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getAge());

    }

    @Test
    void createDeveloperTest_success() {
        //given
        // 중복되는 아이디가 없어야함
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);
        /**
         * save한 것을 캡쳐하기 위함
         * ArgumentCaptor<> : 저장하고자 하는 객체를 잡는 클래스
         * DB에 저장하게 되는 데이터가 무엇인지 확인하고 싶을때,
         * 외부 API로 데이터를 날릴때 어떤 데이터가 날라가는지 확인하고 싶을때
         * → 메소드의 파라미터를 확인할때 사용
         */
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        //when
        CreateDeveloper.Response developer =
                dMakerService.createDeveloper(
                        getCreateRequest(SENIOR, FRONT_END, 12)
                );

        //then
        verify(developerRepository, times(1)) // 특정mock이 몇번 호출되었는지
                .save(captor.capture()); //검증 + repository에 save할 때 넘어가는 파라미터를 직접 캡처
        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_fail_with_unmatched_level() {
        //given
        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(JUNIOR, FRONT_END,
                                MAX_JUNIOR_EXPERIENCE_YEARS + 1)
                )
        );
        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode()
        );

        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(JUNGNIOR, FRONT_END,
                                MIN_SENIOR_EXPERIENCE_YEARS + 1)
                )
        );
        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode()
        );

        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(SENIOR, FRONT_END,
                                MIN_SENIOR_EXPERIENCE_YEARS - 1)
                ) // SENIOR인데 연차가 8년이면 에러가 발생해야 한다.
        );
        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode()
        );
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when
        //then
        /**
         * 응답을 받지 못할 것이기 때문에 assertThrows
         * 해당 exception을 던지게 될 동작을 받게 됨
         * createDeveloper가 만든 exception이 응답 결과로 떨어짐
         */
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(SENIOR, FRONT_END, 12)
                )
        );

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID,
                dMakerException.getDMakerErrorCode());

    }

}