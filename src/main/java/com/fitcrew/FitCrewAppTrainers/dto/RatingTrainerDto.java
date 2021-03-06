package com.fitcrew.FitCrewAppTrainers.dto;

import com.fitcrew.FitCrewAppTrainers.common.ValidationErrorMessage;
import com.fitcrew.FitCrewAppTrainers.dto.validation.NotAllRequiredValueSetInRatingTrainerDto;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
@NotAllRequiredValueSetInRatingTrainerDto
public class RatingTrainerDto {

	@Size(min = 1, max = 10, message = ValidationErrorMessage.RATING_ERROR_MESSAGE)
	private int rating;

	@Size(min = 2, max = 20, message = ValidationErrorMessage.FIRST_NAME_ERROR_MESSAGE)
	private String firstName;

	@Size(min = 2, max = 20, message = ValidationErrorMessage.LAST_NAME_ERROR_MESSAGE)
	private String lastName;
}
