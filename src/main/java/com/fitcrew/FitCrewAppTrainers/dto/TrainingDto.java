package com.fitcrew.FitCrewAppTrainers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel(value = "Training")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class TrainingDto implements Serializable {

    private static final long serialVersionUID = -3255126230685615683L;

    @NotNull(message = "Name of training cannot be null")
    @Size(min = 2, max = 20, message = "Name of training must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Name of training")
    private String trainingName;

    @NotNull(message = "Short description of training cannot be null")
    @Size(min = 2, max = 400, message = "Short description of training must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Short description of training")
    private String description;

    @NotNull(message = "Training cannot be null")
    @Size(min = 2, max = 400, message = "Training must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Training including exercises series and reps")
    private String training;

    @ApiModelProperty(value = "Trainer email address who created this training")
    private String trainerEmail;
}
