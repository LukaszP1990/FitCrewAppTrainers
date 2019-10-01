package com.fitcrew.FitCrewAppTrainers.service.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppTrainers.dao.RatingTrainerDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.google.common.collect.Lists;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerRatingServiceTest {

	private static final List<TrainerEntity> mockedTrainerEntities = TrainerResourceMockUtil.createTrainerEntities();
	private static final TrainerEntity mockedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private static final RatingTrainerEntity mockedRatingTrainerEntity = TrainerResourceMockUtil.createRatingTrainerEntity();
	private static final List<Double> mockedRatingList = Arrays.asList(2.0, 2.0, 2.0);
	private static final List<String> mockedTrainerNamesList = Arrays.asList(
			"firstName lastName3",
			"firstName lastName2",
			"firstName lastName1");
	private final static String TRAINER_ENTITY_EMAIL = "mockedTrainer@gmail.com";

	@Mock
	private TrainerDao trainerDao;

	@Mock
	private RatingTrainerDao ratingTrainerDao;

	@InjectMocks
	private TrainerRatingService trainerRatingService;

	@Test
	void shouldGetRankingOfTrainers() {

		when(trainerDao.findAll())
				.thenReturn(mockedTrainerEntities);

		Either<ErrorMsg, LinkedHashMap<String, Double>> rankingOfTrainers =
				trainerRatingService.getRankingOfTrainers();

		assertNotNull(rankingOfTrainers);

		assertAll(() -> {
					assertTrue(rankingOfTrainers.isRight());
					ArrayList<Double> ratingsList =
							Lists.newArrayList(rankingOfTrainers.get().values());
					ArrayList<String> trainerNamesList = Lists.newArrayList(rankingOfTrainers.get().keySet());

					assertTrue(
							checkRatingsIfEqual(
									mockedRatingList,
									Lists.newArrayList(ratingsList))
					);
					assertTrue(
							checkNamesIfEqual(
									mockedTrainerNamesList,
									Lists.newArrayList(trainerNamesList))
					);
				}
		);
	}

	@Test
	void shouldNotGetRankingOfTrainers() {

		Either<ErrorMsg, LinkedHashMap<String, Double>> noRankingOfTrainers =
				trainerRatingService.getRankingOfTrainers();

		assertNotNull(noRankingOfTrainers);

		checkEitherLeft(true,
				"No trainers sorted",
				noRankingOfTrainers.getLeft());
	}

	@Test
	void shouldGetAverageRatingOfTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);

		Either<ErrorMsg, Double> averageRatingOfTrainer =
				trainerRatingService.getAverageRatingOfTrainer(TRAINER_ENTITY_EMAIL);

		assertNotNull(averageRatingOfTrainer);

		assertAll(() -> {
			assertTrue(averageRatingOfTrainer.isRight());
			assertEquals(new Double(2), averageRatingOfTrainer.get());
		});
	}

	@Test
	void shouldNotGetAverageRatingOfTrainer() {

		Either<ErrorMsg, Double> noAverageRatingOfTrainer =
				trainerRatingService.getAverageRatingOfTrainer(TRAINER_ENTITY_EMAIL);

		assertNotNull(noAverageRatingOfTrainer);

		checkEitherLeft(true,
				"Trainer not found to calculate the average grade",
				noAverageRatingOfTrainer.getLeft());
	}

	@Test
	void shouldSetRateForTheTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);
		when(ratingTrainerDao.save(any()))
				.thenReturn(mockedRatingTrainerEntity);

		Either<ErrorMsg, TrainerDto> trainer =
				trainerRatingService.setRateForTheTrainer(TRAINER_ENTITY_EMAIL, "5");

		assertNotNull(trainer);
		assertTrue(trainer.isRight());
	}

	@Test
	void shouldNotSetRateForTheTrainer() {

		Either<ErrorMsg, TrainerDto> noTrainer =
				trainerRatingService.setRateForTheTrainer(TRAINER_ENTITY_EMAIL, "5");

		assertNotNull(noTrainer);

		checkEitherLeft(true,
				"Trainer not found",
				noTrainer.getLeft());
	}

	private static boolean checkRatingsIfEqual(List<Double> mockedRatingList,
											   List<Double> ratingsList) {
		if (mockedRatingList.size() == ratingsList.size()) {
			for (int i = 0; i < ratingsList.size(); i++) {
				if (!mockedRatingList.get(i).equals(ratingsList.get(i)))
					return false;
				else
					return true;
			}
		}
		return false;
	}

	private static boolean checkNamesIfEqual(List<String> mockedTrainerNamesList,
											 List<String> trainerNamesList) {
		if (mockedTrainerNamesList.size() == trainerNamesList.size()) {
			for (int i = 0; i < trainerNamesList.size(); i++) {
				if (!mockedTrainerNamesList.get(i).equals(trainerNamesList.get(i)))
					return false;
				else
					return true;
			}
		}
		return false;
	}

	private void checkEitherLeft(boolean value,
								 String message,
								 ErrorMsg errorMsg) {
		assertAll(() -> {
			assertTrue(value);
			assertEquals(message, errorMsg.getMsg());
		});
	}
}