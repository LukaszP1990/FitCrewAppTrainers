package com.fitcrew.FitCrewAppTrainers.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fitcrew.FitCrewAppTrainers.domains.EmailEntity;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;

public class TrainerResourceMockUtil {

	public static TrainerEntity createTrainerEntity() {
		return prepareTrainerEntityData("lastName");
	}

	public static List<TrainerEntity> createTrainerEntities() {
		return IntStream.rangeClosed(1, 3)
				.mapToObj(value -> prepareTrainerEntityData("lastName" + value))
				.collect(Collectors.toList());
	}

	public static TrainerDto createTrainerDto() {
		return TrainerDto.builder()
				.firstName("firstName")
				.lastName("lastName")
				.dateOfBirth("01.01.1990")
				.password("test")
				.encryptedPassword("$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei")
				.email("mockedTrainer@gmail.com")
				.phone("501928341")
				.somethingAboutYourself("Description about mock trainer")
				.build();
	}

	public static List<TrainerDto> createTrainerDtos() {
		return IntStream.rangeClosed(1, 3)
				.mapToObj(value -> prepareTrainerDtoData(
						"firstName",
						"lastName" + value,
						"01.01.1990",
						"test",
						"mockedTrainer@gmail.com",
						"501928341"))
				.collect(Collectors.toList());
	}


	public static TrainerDto updateTrainerDto() {
		return prepareTrainerDtoData(
				"firstName",
				"lastName",
				"01.01.1990",
				"test",
				"mockedTrainer@gmail.com",
				"501928341");
	}

	private static List<RatingTrainerEntity> createRatingTrainerEntities() {
		return IntStream.rangeClosed(1, 3)
				.mapToObj(TrainerResourceMockUtil::prepareRatingTrainerEntityData)
				.collect(Collectors.toList());
	}

	public static RatingTrainerEntity createRatingTrainerEntity() {
		return prepareRatingTrainerEntityData(2);
	}

	public static EmailEntity createEmailEntity() {
		return EmailEntity.builder()
				.sender("senderTest")
				.recipient("firstName lastName")
				.subject("Test message")
				.bodyOfMessage("Hi this is a test message")
				.build();
	}

	public static EmailDto createEmailDto() {
		return EmailDto.builder()
				.sender("senderTest")
				.recipient("firstName lastName")
				.subject("Test message")
				.bodyOfMessage("Hi this is a test message")
				.build();
	}

	private static RatingTrainerEntity prepareRatingTrainerEntityData(int rating) {
		return RatingTrainerEntity.builder()
				.rating(rating)
				.firstName("mockedName")
				.lastName("mockedName")
				.build();
	}

	private static TrainerEntity prepareTrainerEntityData(String lastName) {
		return TrainerEntity.builder()
				.firstName("firstName")
				.lastName(lastName)
				.dateOfBirth("01.01.1990")
				.encryptedPassword("$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei")
				.email("mockedTrainer@gmail.com")
				.phone("501928341")
				.somethingAboutYourself("Description about mock trainer")
				.ratingTrainerEntity(createRatingTrainerEntities())
				.build();
	}

	private static TrainerDto prepareTrainerDtoData(String firstName,
													String lastName,
													String dateOfBirth,
													String password,
													String email,
													String phone) {
		return TrainerDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.dateOfBirth(dateOfBirth)
				.password(password)
				.encryptedPassword("$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei")
				.email(email)
				.phone(phone)
				.build();
	}
}
