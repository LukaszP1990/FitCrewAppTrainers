package com.fitcrew.FitCrewAppTrainers.dto;

import com.fitcrew.FitCrewAppTrainers.common.ValidationErrorMessage;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class LoginDto {

    @NotNull(message = ValidationErrorMessage.LOGIN_ERROR_MESSAGE)
    private String email;

    @NotNull(message = ValidationErrorMessage.PASSWORD_ERROR_MESSAGE)
    private String password;
}
