package com.fitcrew.FitCrewAppTrainers.dto;

import com.fitcrew.FitCrewAppTrainers.common.ValidationErrorMessage;
import com.fitcrew.FitCrewAppTrainers.dto.validation.NotAllRequiredValueSetInTrainerDto;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
@NotAllRequiredValueSetInTrainerDto
public class TrainerDto implements Serializable {

    private static final long serialVersionUID = -3255126230685615683L;

    @Size(min = 2, max = 20, message = ValidationErrorMessage.FIRST_NAME_ERROR_MESSAGE)
    private String firstName;

    @Size(min = 2, max = 20, message = ValidationErrorMessage.LAST_NAME_ERROR_MESSAGE)
    private String lastName;

    private String email;
    private String phone;

    @Size(min = 2, max = 20, message = ValidationErrorMessage.DATE_OF_BIRTH_ERROR_MESSAGE)
    private String dateOfBirth;

    @Size(min = 2, max = 20, message = ValidationErrorMessage.TYPE_OF_TRAINING_ERROR_MESSAGE)
    private String typeOfTraining;

    private String placeInTheRanking;

    @Size(min = 2, max = 200, message = ValidationErrorMessage.TRAINER_DESCRIPTION_ERROR_MESSAGE)
    private String somethingAboutYourself;

    private String password;
    private String trainerId;
    private String encryptedPassword;
}
