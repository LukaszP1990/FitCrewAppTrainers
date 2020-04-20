package com.fitcrew.FitCrewAppTrainers.resource.converter;

import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainerDocumentTrainerDtoConverterTest {

    private static TrainerDto trainerDto = TrainerResourceMockUtil.createTrainerDto();
    private TrainerDocumentTrainerDtoConverter trainerConverter = Mappers.getMapper(TrainerDocumentTrainerDtoConverter.class);
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
    private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    private static String TRAINER_PHONE_NUMBER = "501928341";
    private static String TRAINER_DESCRIPTION = "Description about mock trainer";

    @Test
    void shouldConvertTrainerDtoToTrainerDocument() {
        TrainerDocument trainerDocument = trainerConverter.trainerDtoToTrainerDocument(trainerDto);
        assertNotNull(trainerDocument);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, trainerDocument.getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainerDocument.getLastName());
            assertEquals(TRAINER_EMAIL, trainerDocument.getEmail());
            assertEquals(ENCRYPTED_PASSWORD, trainerDocument.getEncryptedPassword());
            assertEquals(TRAINER_DATE_OF_BIRTH, trainerDocument.getDateOfBirth());
            assertEquals(TRAINER_PHONE_NUMBER, trainerDocument.getPhone());
            assertEquals(TRAINER_DESCRIPTION, trainerDocument.getSomethingAboutYourself());
        });
    }
}
