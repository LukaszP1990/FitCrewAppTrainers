package com.fitcrew.FitCrewAppTrainers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel(value = "Login")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class LoginDto {

	@NotNull(message = "Login cannot be null")
	@ApiModelProperty(value = "Email address. It's a login name for trainer")
	private String email;

	@NotNull(message = "Password cannot be null")
	@ApiModelProperty(value = "Password to authorize client")
	private String password;
}
