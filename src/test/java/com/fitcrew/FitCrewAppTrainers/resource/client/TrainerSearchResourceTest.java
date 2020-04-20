package com.fitcrew.FitCrewAppTrainers.resource.client;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.resource.AbstractRestResourceTest;
import com.fitcrew.FitCrewAppTrainers.util.HttpEntityUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class TrainerSearchResourceTest extends AbstractRestResourceTest {

    private static final List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
    private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_DESCRIPTION = "Description about mock trainer";
    private static String TRAINER_PHONE_NUMBER = "501928341";

    @MockBean
    private TrainerDao trainerDao;

    @Test
    void getTrainers() {
        Mockito.when(trainerDao.findAll()).thenReturn(mockedTrainerDocuments);

        ResponseEntity<List<TrainerModel>> responseEntity = restTemplate.exchange(
                "/trainer/search/getTrainers",
                HttpMethod.GET,
                HttpEntityUtil.getHttpEntityWithJwt(),
                new ParameterizedTypeReference<>() {
                }
        );

        List<TrainerModel> trainers = responseEntity.getBody();
        assertNotNull(trainers);
        assertEquals(3, trainers.size());
    }

    @Test
    void getTrainer() {
        Mockito.when(trainerDao.findByEmail(anyString())).thenReturn(Optional.of(mockedTrainerDocument));

        ResponseEntity<TrainerModel> responseEntity = restTemplate.exchange(
                "/trainer/search/getTrainer/" + TRAINER_EMAIL + "/trainerEmail",
                HttpMethod.GET,
                HttpEntityUtil.getHttpEntityWithJwt(),
                TrainerModel.class
        );

        TrainerModel trainerDto = responseEntity.getBody();
        assertNotNull(trainerDto);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, trainerDto.getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainerDto.getLastName());
            assertEquals(TRAINER_EMAIL, trainerDto.getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, trainerDto.getPhone());
            assertEquals(TRAINER_DESCRIPTION, trainerDto.getSomethingAboutYourself());
        });
    }
}