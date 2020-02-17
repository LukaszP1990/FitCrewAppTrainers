package com.fitcrew.FitCrewAppTrainers.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "ClientResponses")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class ClientResponsesDto implements Serializable {

	private static final long serialVersionUID = 2118331620631970477L;

	@NotNull(message = "Client name cannot be null")
	@Size(min = 2, max = 20, message = "Client name must be equal or grater than 2 characters and less than 20 character")
	@ApiModelProperty(value = "Client name sender")
	private String clientName;

	@NotNull(message = "Client message cannot be null")
	@ApiModelProperty(value = "Body of email message")
	private String bodyOfMessage;

}
