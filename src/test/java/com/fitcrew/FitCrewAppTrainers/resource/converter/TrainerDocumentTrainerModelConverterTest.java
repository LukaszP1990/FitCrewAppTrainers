package com.fitcrew.FitCrewAppTrainers.resource.converter;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDocumentTrainerModelConverterTest {

    private static TrainerDocument trainerDocument = TrainerResourceMockUtil.createTrainerDocument();
    private TrainerDocumentTrainerModelConverter trainerConverter = Mappers.getMapper(TrainerDocumentTrainerModelConverter.class);
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
    private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    private static String TRAINER_PHONE_NUMBER = "501928341";
    private static String TRAINER_DESCRIPTION = "Description about mock trainer";

    @Test
    void shouldConvertTrainerDtoToTrainerDocument() {
        TrainerModel trainerModel = trainerConverter.trainerDocumentToTrainerModel(trainerDocument);
        assertNotNull(trainerModel);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, trainerModel.getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainerModel.getLastName());
            assertEquals(TRAINER_EMAIL, trainerModel.getEmail());
            assertEquals(ENCRYPTED_PASSWORD, trainerModel.getEncryptedPassword());
            assertEquals(TRAINER_DATE_OF_BIRTH, trainerModel.getDateOfBirth());
            assertEquals(TRAINER_PHONE_NUMBER, trainerModel.getPhone());
            assertEquals(TRAINER_DESCRIPTION, trainerModel.getSomethingAboutYourself());
        });
    }
}
