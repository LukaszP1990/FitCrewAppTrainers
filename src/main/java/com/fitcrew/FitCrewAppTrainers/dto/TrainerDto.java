package com.fitcrew.FitCrewAppTrainers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel(value = "Trainer")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class TrainerDto implements Serializable {

    private static final long serialVersionUID = -3255126230685615683L;

    @NotNull(message = "First name of trainer cannot be null")
    @Size(min = 2, max = 20, message = "First name of trainer must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "First name of trainer")
    private String firstName;

    @NotNull(message = "Last name of trainer cannot be null")
    @Size(min = 2, max = 20, message = "Last name of trainer must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Last name of trainer")
    private String lastName;

    @NotNull(message = "Trainer email address cannot be null")
    @ApiModelProperty(value = "Trainer email address")
    private String email;

    @NotNull(message = "Trainer phone number cannot be null")
    @ApiModelProperty(value = "Trainer phone number")
    private String phone;

    @NotNull(message = "Date of birth cannot be null")
    @Size(min = 2, max = 20, message = "Date of birth must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Trainer date of birth")
    private String dateOfBirth;

    @NotNull(message = "Short description about trainer cannot be null")
    @Size(min = 2, max = 200, message = "Last name of trainer must be equal or grater than 2 characters and less than 200 character")
    @ApiModelProperty(value = "Short description about trainer")
    private String somethingAboutYourself;

    @NotNull(message = "Password cannot be null")
    @ApiModelProperty(value = "Trainer password")
    private String password;

    @ApiModelProperty(value = "Trainer number identification")
    private String trainerId;

    @ApiModelProperty(value = "Trainer encrypted password")
    private String encryptedPassword;
}
