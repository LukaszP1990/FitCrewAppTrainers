package com.fitcrew.FitCrewAppTrainers.util;

import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.dto.RatingTrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainerResourceMockUtil {

    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String SENDER = "senderTest";
    private static String RECIPIENT = "firstName lastName";
    private static String SUBJECT = "Test message";
    private static String BODY_OF_MESSAGE = "Hi this is a test message";
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
    private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    private static String TRAINER_PHONE_NUMBER = "501928341";
    private static String TRAINER_DESCRIPTION = "Description about mock trainer";

    public static TrainerDocument createTrainerDocument() {
        return prepareTrainerDocumentData(TRAINER_LAST_NAME, 1);
    }

    public static List<TrainerDocument> createTrainerDocuments() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(value -> prepareTrainerDocumentData(TRAINER_LAST_NAME + value, value))
                .collect(Collectors.toList());
    }

    public static TrainerDto createTrainerDto() {
        return TrainerDto.builder()
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .dateOfBirth(TRAINER_DATE_OF_BIRTH)
                .password("test")
                .encryptedPassword(ENCRYPTED_PASSWORD)
                .email(TRAINER_EMAIL)
                .phone(TRAINER_PHONE_NUMBER)
                .typeOfTraining("mock")
                .placeInTheRanking("1")
                .somethingAboutYourself(TRAINER_DESCRIPTION)
                .build();
    }

    public static EmailDto sendNotValidEmailDto() {
        return EmailDto.builder()
                .sender(SENDER)
                .build();
    }

    public static RatingTrainerDto getRatingTrainerDto() {
        return RatingTrainerDto.builder()
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .rating(10)
                .build();
    }

    public static RatingTrainerDto getNotValidRatingTrainerDto() {
        return RatingTrainerDto.builder()
                .firstName(TRAINER_FIRST_NAME)
                .build();
    }

    public static TrainerDto createNotValidTrainerDto() {
        return TrainerDto.builder()
                .firstName(TRAINER_FIRST_NAME)
                .build();
    }

    public static TrainerDto updateTrainerDto() {
        return prepareTrainerDtoData(
        );
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
                .sender(SENDER)
                .recipient(RECIPIENT)
                .subject(SUBJECT)
                .bodyOfMessage(BODY_OF_MESSAGE)
                .build();
    }

    public static EmailDto createEmailDto() {
        return EmailDto.builder()
                .sender(SENDER)
                .recipient(RECIPIENT)
                .subject(SUBJECT)
                .bodyOfMessage(BODY_OF_MESSAGE)
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
                .firstName(TRAINER_FIRST_NAME)
                .lastName(lastName)
                .dateOfBirth(TRAINER_DATE_OF_BIRTH)
                .encryptedPassword(ENCRYPTED_PASSWORD)
                .email(TRAINER_EMAIL)
                .phone(TRAINER_PHONE_NUMBER)
                .somethingAboutYourself(TRAINER_DESCRIPTION)
                .build();
    }

    private static TrainerDto prepareTrainerDtoData() {
        return TrainerDto.builder()
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .dateOfBirth(TRAINER_DATE_OF_BIRTH)
                .password("test")
                .encryptedPassword(ENCRYPTED_PASSWORD)
                .email(TRAINER_EMAIL)
                .phone(TRAINER_PHONE_NUMBER)
                .build();
    }
}
