package com.fitcrew.FitCrewAppTrainers.dto;

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

@ApiModel(value = "Rating Trainer")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class RatingTrainerDto {

	@NotNull(message = "Rating amount cannot be null")
	@Size(min = 1, max = 10, message = "Rating amount must be equal or grater than 1 characters and less than 10 character")
	@ApiModelProperty(value = "Rating amount")
	private int rating;

	@NotNull(message = "Trainer first name cannot be null")
	@Size(min = 2, max = 20, message = "Trainer first name must be equal or grater than 2 characters and less than 20 character")
	@ApiModelProperty(value = "Trainer first name")
	private String firstName;

	@NotNull(message = "Trainer last name cannot be null")
	@Size(min = 2, max = 20, message = "Trainer last name must be equal or grater than 2 characters and less than 20 character")
	@ApiModelProperty(value = "Trainer last name")
	private String lastName;
}
