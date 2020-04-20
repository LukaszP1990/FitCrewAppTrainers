package com.fitcrew.FitCrewAppTrainers.resource.admin;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resource.AbstractRestResourceTest;
import com.fitcrew.FitCrewAppTrainers.util.HttpEntityUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TrainerAdminResourceTest extends AbstractRestResourceTest {

    private final static List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
    private final static TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
    private final static TrainerDto mockedTrainerDto = TrainerResourceMockUtil.createTrainerDto();
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    private static String TRAINER_PHONE_NUMBER = "501928341";

    @MockBean
    private TrainerDao trainerDao;

    @Test
    void shouldGetTrainers() {

        when(trainerDao.findAll())
                .thenReturn(mockedTrainerDocuments);

        ResponseEntity<List<TrainerDto>> responseEntity = restTemplate.exchange(
                "/trainer/getTrainers",
                HttpMethod.GET,
                HttpEntityUtil.getHttpEntityWithJwt(),
                new ParameterizedTypeReference<>() {
                }
        );

        assertNotNull(responseEntity.getBody());
        assertEquals(3, responseEntity.getBody().size());
    }

    @Test
    void shouldGetTrainer() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        ResponseEntity<TrainerDto> responseEntity = restTemplate.exchange(
                "/trainer/getTrainer/" + TRAINER_EMAIL + "/trainerEmail",
                HttpMethod.GET,
                HttpEntityUtil.getHttpEntityWithJwt(),
                TrainerDto.class
        );

        TrainerDto trainer = responseEntity.getBody();
        checkAssertions(trainer);
    }

    @Test
    void shouldDeleteTrainer() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        ResponseEntity<TrainerDto> responseEntity = restTemplate.exchange(
                "/trainer/deleteTrainer/" + TRAINER_EMAIL + "/trainerEmail",
                HttpMethod.DELETE,
                HttpEntityUtil.getHttpEntityWithJwtAndBody(mockedTrainerDto),
                TrainerDto.class
        );

        TrainerDto trainer = responseEntity.getBody();
        checkAssertions(trainer);
    }

    @Test
    void updateTrainer() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        ResponseEntity<TrainerDto> responseEntity = restTemplate.exchange(
                "/trainer/updateTrainer/" + TRAINER_EMAIL + "/trainerEmail",
                HttpMethod.PUT,
                HttpEntityUtil.getHttpEntityWithJwtAndBody(mockedTrainerDto),
                TrainerDto.class
        );

        TrainerDto trainer = responseEntity.getBody();
        checkAssertions(trainer);
    }

    private void checkAssertions(TrainerDto trainerDto) {
        assertNotNull(trainerDto);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, trainerDto.getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainerDto.getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, trainerDto.getDateOfBirth());
            assertEquals(
                    ENCRYPTED_PASSWORD,
                    trainerDto.getEncryptedPassword()
            );
            assertEquals(TRAINER_EMAIL, trainerDto.getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, trainerDto.getPhone());
        });
    }
}