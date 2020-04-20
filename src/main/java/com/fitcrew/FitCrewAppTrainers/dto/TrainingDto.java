package com.fitcrew.FitCrewAppTrainers.dto;

import com.fitcrew.FitCrewAppTrainers.common.ValidationErrorMessage;
import com.fitcrew.FitCrewAppTrainers.dto.validation.NotAllRequiredValueSetInTrainingDto;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
@NotAllRequiredValueSetInTrainingDto
public class TrainingDto implements Serializable {

    private static final long serialVersionUID = -3255126230685615683L;

    @Size(min = 2, max = 20, message = ValidationErrorMessage.TRAINING_NAME_ERROR_MESSAGE)
    private String trainingName;

    @Size(min = 2, max = 400, message = ValidationErrorMessage.TRAINING_DESCRIPTION_ERROR_MESSAGE)
    private String description;

    @Size(min = 2, message = ValidationErrorMessage.TRAINING_ERROR_MESSAGE)
    private String training;

    private String trainerEmail;
    private List<String> clients;
}
