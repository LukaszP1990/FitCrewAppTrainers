package com.fitcrew.FitCrewAppTrainers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel(value = "Client")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class ClientDto implements Serializable {

    private static final long serialVersionUID = 2118331620631970477L;

    @NotNull(message = "First trainingName of client cannot be null")
    @Size(min = 2, max = 20, message = "First trainingName of client must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "First trainingName of client")
    private String firstName;

    @NotNull(message = "Last trainingName of client cannot be null")
    @Size(min = 2, max = 20, message = "Last trainingName of client must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "First trainingName of client")
    private String lastName;

    @NotNull(message = "Client email address cannot be null")
    @ApiModelProperty(value = "Client email address")
    private String email;

    @NotNull(message = "Client phone number cannot be null")
    @ApiModelProperty(value = "Client phone number")
    private String phone;

    @NotNull(message = "Date of birth cannot be null")
    @Size(min = 2, max = 20, message = "Date of birth must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Date when client was born")
    private String dateOfBirth;

    @NotNull(message = "Password cannot be null")
    @ApiModelProperty(value = "Client password")
    private String password;

    @ApiModelProperty(value = "Client number identification")
    private String clientId;

    @ApiModelProperty(value = "Client encrypted password")
    private String encryptedPassword;
}
