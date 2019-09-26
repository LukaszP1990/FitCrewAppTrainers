package com.fitcrew.FitCrewAppTrainers.domains;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Trainer")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class TrainerEntity implements Serializable {

    private static final long serialVersionUID = 1421658171867127534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainerEntityId")
    private long trainerEntityId;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(name = "Email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false, length = 20)
    private String dateOfBirth;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "[0-9]{9}")
    private String phone;

    @Column(nullable = false, unique = true)
    private String placeInTheRanking;

    @Column(nullable = false, length = 200)
    private String somethingAboutYourself;

    @Column(nullable = false, unique = true)
    private String trainerId;

    @Column(nullable = false, unique = true)
    private String encryptedPassword;

    @OneToMany(mappedBy = "ratingTrainerEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RatingTrainerEntity> ratingTrainerEntity;

}
