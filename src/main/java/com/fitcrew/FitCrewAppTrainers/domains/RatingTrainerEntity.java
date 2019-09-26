package com.fitcrew.FitCrewAppTrainers.domains;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModel;
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
public
class RatingTrainerEntity implements Serializable {

	private static final long serialVersionUID = 1421658171867127534L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ratingTrainerId;

	@Column(nullable = false)
	private int rating;

	@Column(nullable = false, length = 20)
	private String firstName;

	@Column(nullable = false, length = 20)
	private String lastName;

	@ManyToOne
	@JoinColumn(name = "trainerEntityId")
	private RatingTrainerEntity ratingTrainerEntity;
}
