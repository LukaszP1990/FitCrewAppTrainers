package com.fitcrew.FitCrewAppTrainers.validation;

import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.validation.NotAllRequiredValueSetInTrainerDto;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotAllRequiredValueSetInTrainerDtoTest {

    private static TrainerDto validTrainerDto = TrainerResourceMockUtil.createTrainerDto();
    private static TrainerDto notValidTrainerDto = TrainerResourceMockUtil.createNotValidTrainerDto();

    @Mock
    private NotAllRequiredValueSetInTrainerDto.NotAllRequiredValueSetInTrainerDtoValidator trainerValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        when(trainerValidator.isValid(any(), any())).thenCallRealMethod();
    }

    @Test
    void shouldSucceedWhenRequiredValuesHaveBeenSet() {

        assertTrue(trainerValidator
                .isValid(validTrainerDto, constraintValidatorContext));
    }

    @Test
    void shouldFailWhenWhenRequiredValuesHaveNotBeenSet() {
        assertFalse(trainerValidator
                .isValid(notValidTrainerDto, constraintValidatorContext));
    }
}
