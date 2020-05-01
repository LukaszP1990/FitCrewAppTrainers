package com.fitcrew.FitCrewAppTrainers.validation;

import com.fitcrew.FitCrewAppTrainers.dto.RatingTrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.validation.NotAllRequiredValueSetInRatingTrainerDto;
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
class NotAllRequiredValueSetInRatingTrainerDtoTest {

    private static RatingTrainerDto validRatingTrainerDto = TrainerResourceMockUtil.getRatingTrainerDto();
    private static RatingTrainerDto notValidRatingTrainerDto = TrainerResourceMockUtil.getNotValidRatingTrainerDto();

    @Mock
    private NotAllRequiredValueSetInRatingTrainerDto.NotAllRequiredValueSetInRatingTrainerDtoValidator ratingTrainerValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        when(ratingTrainerValidator.isValid(any(), any())).thenCallRealMethod();
    }

    @Test
    void shouldSucceedWhenRequiredValuesHaveBeenSet() {

        assertTrue(ratingTrainerValidator
                .isValid(validRatingTrainerDto, constraintValidatorContext));
    }

    @Test
    void shouldFailWhenWhenRequiredValuesHaveNotBeenSet() {
        assertFalse(ratingTrainerValidator
                .isValid(notValidRatingTrainerDto, constraintValidatorContext));
    }
}
