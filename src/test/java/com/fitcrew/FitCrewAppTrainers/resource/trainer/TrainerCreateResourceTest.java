package com.fitcrew.FitCrewAppTrainers.resource.trainer;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resource.AbstractRestResourceTest;
import com.fitcrew.FitCrewAppTrainers.util.HttpEntityUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class TrainerCreateResourceTest extends AbstractRestResourceTest {

    private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
    private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
    private static final TrainerDto mockedTrainerDto = TrainerResourceMockUtil.createTrainerDto();
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    private static String TRAINER_PHONE_NUMBER = "501928341";
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";

    @MockBean
    private TrainerDao trainerDao;

    @Test
    void createTrainer() {

        Mockito.when(trainerDao.save(any())).thenReturn(mockedTrainerDocument);

        ResponseEntity<TrainerDto> responseEntity = restTemplate.postForEntity(
                "/trainer/createTrainer",
                HttpEntityUtil.getHttpEntityWithJwtAndBody(mockedTrainerDto),
                TrainerDto.class);

        TrainerDto trainerDto = responseEntity.getBody();

        assertNotNull(trainerDto);

        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, trainerDto.getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainerDto.getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, trainerDto.getDateOfBirth());
            assertEquals(ENCRYPTED_PASSWORD, trainerDto.getEncryptedPassword());
            assertEquals(TRAINER_EMAIL, trainerDto.getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, trainerDto.getPhone());
        });
    }
}