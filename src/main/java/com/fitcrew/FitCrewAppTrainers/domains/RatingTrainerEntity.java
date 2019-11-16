package com.fitcrew.FitCrewAppTrainers.domains;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Rating")
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
	private TrainerEntity trainerEntity;
}
