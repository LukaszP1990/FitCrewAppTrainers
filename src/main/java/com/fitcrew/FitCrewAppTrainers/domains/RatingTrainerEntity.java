package com.fitcrew.FitCrewAppTrainers.domains;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public
class RatingTrainerEntity implements Serializable {

	private static final long serialVersionUID = 1421658171867127534L;

	@Id
	private long ratingTrainerId;

	@Field(value = "RATING")
	@NotNull
	private int rating;

	@Field(value = "FIRST_NAME")
	@NotNull
	@Length(max = 20)
	private String firstName;

	@Field(value = "LAST_NAME")
	@NotNull
	@Length(max = 20)
	private String lastName;

	@Field(value = "TRAINER_ID")
	@NotNull
	private Long trainerId;
}
