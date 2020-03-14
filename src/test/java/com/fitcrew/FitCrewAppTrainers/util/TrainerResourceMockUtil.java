package com.fitcrew.FitCrewAppTrainers.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fitcrew.FitCrewAppModel.domain.model.EmailDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;

public class TrainerResourceMockUtil {

	public static TrainerDocument createTrainerDocument() {
		return prepareTrainerDocumentData("lastName", 1);
	}

	public static List<TrainerDocument> createTrainerDocuments() {
		return IntStream.rangeClosed(1, 3)
				.mapToObj(value -> prepareTrainerDocumentData("lastName" + value, value))
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

	public static LinkedHashMap<String, Double> getSortedTrainersRanking() {
		LinkedHashMap<String, Double> sortedTrainersRanking = new LinkedHashMap<>();
		sortedTrainersRanking.put("firstName", 9d);
		sortedTrainersRanking.put("secondName", 10d);
		return sortedTrainersRanking;
	}

	public static List<RatingTrainerDocument> createRatingTrainerDocuments() {
		return IntStream.rangeClosed(1, 3)
				.mapToObj(TrainerResourceMockUtil::prepareRatingTrainerDocumentData)
				.collect(Collectors.toList());
	}

	public static RatingTrainerDocument createRatingTrainerDocument() {
		return prepareRatingTrainerDocumentData(2);
	}

	public static EmailDocument createEmailDocument() {
		return EmailDocument.builder()
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

	private static RatingTrainerDocument prepareRatingTrainerDocumentData(int rating) {
		return RatingTrainerDocument.builder()
				.rating(rating)
				.firstName("mockedName")
				.lastName("mockedName")
				.build();
	}

	private static TrainerDocument prepareTrainerDocumentData(String lastName, int id) {
		return TrainerDocument.builder()
				.id(String.valueOf(id))
				.firstName("firstName")
				.lastName(lastName)
				.dateOfBirth("01.01.1990")
				.encryptedPassword("$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei")
				.email("mockedTrainer@gmail.com")
				.phone("501928341")
				.somethingAboutYourself("Description about mock trainer")
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
