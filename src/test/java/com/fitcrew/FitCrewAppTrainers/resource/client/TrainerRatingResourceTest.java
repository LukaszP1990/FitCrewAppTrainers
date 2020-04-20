package com.fitcrew.FitCrewAppTrainers.resource.client;

import com.fitcrew.FitCrewAppTrainers.dao.RatingTrainerDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.RatingTrainerDto;
import com.fitcrew.FitCrewAppTrainers.resource.AbstractRestResourceTest;
import com.fitcrew.FitCrewAppTrainers.util.HttpEntityUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class TrainerRatingResourceTest extends AbstractRestResourceTest {

    private static final List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
    private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
    private static final List<RatingTrainerDocument> mockedRatingTrainerDocuments = TrainerResourceMockUtil.createRatingTrainerDocuments();
    private static final RatingTrainerDocument mockedRatingTrainerDocument = TrainerResourceMockUtil.createRatingTrainerDocument();
    private static String TRAINER_DOCUMENT_EMAIL = "mockedTrainer@gmail.com";

    @MockBean
    private TrainerDao trainerDao;

    @MockBean
    private RatingTrainerDao ratingTrainerDao;

    @Test
    void shouldGetTrainerRating() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));
        when(ratingTrainerDao.findByTrainerId(anyLong()))
                .thenReturn(Optional.of(mockedRatingTrainerDocuments));

        ResponseEntity<Double> responseEntity = restTemplate.exchange(
                "/trainer/rate/getTrainerRating/" + TRAINER_DOCUMENT_EMAIL + "/trainerEmail",
                HttpMethod.GET,
                HttpEntityUtil.getHttpEntityWithJwt(),
                Double.class
        );

        Double averageRatingOfTrainer = responseEntity.getBody();
        assertNotNull(averageRatingOfTrainer);
        assertEquals(new Double(2), averageRatingOfTrainer);
    }

    @Test
    void shouldGetTrainersRanking() {

        when(trainerDao.findAll())
                .thenReturn(mockedTrainerDocuments);
        when(ratingTrainerDao.findByTrainerId(anyLong()))
                .thenReturn(Optional.of(mockedRatingTrainerDocuments));

        ResponseEntity<LinkedHashMap<String, Double>> responseEntity = restTemplate.exchange(
                "/trainer/rate/getTrainersRanking",
                HttpMethod.GET,
                HttpEntityUtil.getHttpEntityWithJwt(),
                new ParameterizedTypeReference<>() {
                }
        );

        LinkedHashMap<String, Double> rankingOfTrainers = responseEntity.getBody();

        assertNotNull(rankingOfTrainers);
        assertAll(() -> {
                    ArrayList<Double> ratingsList =
                            Lists.newArrayList(rankingOfTrainers.values());
                    ArrayList<String> trainerNamesList = Lists.newArrayList(rankingOfTrainers.keySet());

                    assertFalse(ratingsList.isEmpty());
                    assertFalse(trainerNamesList.isEmpty());
                }
        );
    }

    @Test
    void shouldRateTheTrainer() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));
        when(ratingTrainerDao.save(any()))
                .thenReturn(mockedRatingTrainerDocument);

        ResponseEntity<RatingTrainerDto> responseEntity = restTemplate.exchange(
                "/trainer/rate/rateTheTrainer/" + TRAINER_DOCUMENT_EMAIL + "/trainerEmail/5/ratingForTrainer",
                HttpMethod.POST,
                HttpEntityUtil.getHttpEntityWithJwt(),
                RatingTrainerDto.class
        );

        assertNotNull(responseEntity.getBody());
    }

}