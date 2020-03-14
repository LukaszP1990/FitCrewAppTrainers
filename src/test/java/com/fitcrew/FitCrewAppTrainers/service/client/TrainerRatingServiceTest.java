package com.fitcrew.FitCrewAppTrainers.service.client;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerDto;
import com.fitcrew.FitCrewAppTrainers.converter.RatingTrainerDocumentRatingTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.converter.RatingTrainerDocumentRatingTrainerDtoConverterImpl;
import com.fitcrew.FitCrewAppTrainers.dao.RatingTrainerDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.google.common.collect.Lists;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerRatingServiceTest {

	private static final List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
	private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
	private static final RatingTrainerDocument mockedRatingTrainerDocument = TrainerResourceMockUtil.createRatingTrainerDocument();
	private static final List<Double> mockedRatingList = Arrays.asList(2.0, 2.0, 2.0);
	private static final List<String> mockedTrainerNamesList = Arrays.asList(
			"firstName lastName3",
			"firstName lastName2",
			"firstName lastName1");
	private static String TRAINER_DOCUMENT_EMAIL = "mockedTrainer@gmail.com";

	@Captor
	private ArgumentCaptor<String> stringArgumentCaptor;

	@Captor
	private ArgumentCaptor<RatingTrainerDocument> ratingTrainerDocumentArgumentCaptor;

	private TrainerDao trainerDao = Mockito.mock(TrainerDao.class);
	private RatingTrainerDao ratingTrainerDao = Mockito.mock(RatingTrainerDao.class);
	private RatingTrainerDocumentRatingTrainerDtoConverter ratingTrainerConverter = new RatingTrainerDocumentRatingTrainerDtoConverterImpl();

	private TrainerRatingService trainerRatingService = new TrainerRatingService(trainerDao, ratingTrainerDao, ratingTrainerConverter);

	@Test
	void shouldGetRankingOfTrainers() {

		when(trainerDao.findAll())
				.thenReturn(mockedTrainerDocuments);
		when(ratingTrainerDao.findByTrainerId(any()))
				.thenReturn(Optional.of(Lists.newArrayList(mockedRatingTrainerDocument)));

		Either<ErrorMsg, LinkedHashMap<String, Double>> rankingOfTrainers =
				trainerRatingService.getRankingOfTrainers();

		verify(trainerDao, times(1)).findAll();

		assertNotNull(rankingOfTrainers);
		assertAll(() -> {
					assertTrue(rankingOfTrainers.isRight());
					ArrayList<Double> ratingsList =
							Lists.newArrayList(rankingOfTrainers.get().values());
					ArrayList<String> trainerNamesList = Lists.newArrayList(rankingOfTrainers.get().keySet());

					assertFalse(ratingsList.isEmpty());
					assertFalse(trainerNamesList.isEmpty());
					assertTrue(
							checkRatingsIfEqual(Lists.newArrayList(ratingsList))
					);
					assertTrue(
							checkNamesIfEqual(
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
		checkEitherLeft(noRankingOfTrainers.isLeft(), TrainerErrorMessageType.NO_TRAINERS_SORTED, noRankingOfTrainers.getLeft());
	}

	@Test
	void shouldGetAverageRatingOfTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerDocument));
		when(ratingTrainerDao.findByTrainerId(any()))
				.thenReturn(Optional.of(Lists.newArrayList(mockedRatingTrainerDocument)));

		Either<ErrorMsg, Double> averageRatingOfTrainer =
				trainerRatingService.getAverageRatingOfTrainer(TRAINER_DOCUMENT_EMAIL);

		verifyFindDocumentByEmail();
		assertNotNull(averageRatingOfTrainer);
		assertAll(() -> {
			assertTrue(averageRatingOfTrainer.isRight());
			assertEquals(new Double(2), averageRatingOfTrainer.get());
		});
	}


	@Test
	void shouldNotGetAverageRatingOfTrainer() {

		Either<ErrorMsg, Double> noAverageRatingOfTrainer =
				trainerRatingService.getAverageRatingOfTrainer(TRAINER_DOCUMENT_EMAIL);

		assertNotNull(noAverageRatingOfTrainer);
		checkEitherLeft(noAverageRatingOfTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER, noAverageRatingOfTrainer.getLeft());
	}

	@Test
	void shouldSetRateForTheTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerDocument));
		when(ratingTrainerDao.save(any()))
				.thenReturn(mockedRatingTrainerDocument);

		Either<ErrorMsg, RatingTrainerDto> ratedTrainer =
				trainerRatingService.setRateForTheTrainer(TRAINER_DOCUMENT_EMAIL, "5");

		verifyFindDocumentByEmail();
		verifySaveTrainerDocument();
		assertNotNull(ratedTrainer);
		assertTrue(ratedTrainer.isRight());
	}

	@Test
	void shouldNotSetRateForTheTrainer() {

		Either<ErrorMsg, RatingTrainerDto> noRatedTrainer =
				trainerRatingService.setRateForTheTrainer(TRAINER_DOCUMENT_EMAIL, "5");

		assertNotNull(noRatedTrainer);
		checkEitherLeft(noRatedTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER, noRatedTrainer.getLeft());
	}

	private static boolean checkRatingsIfEqual(List<Double> ratingsList) {
		return Optional.ofNullable(ratingsList)
				.filter(ratings -> !ratings.isEmpty())
				.filter(ratings -> mockedRatingList.size() == ratings.size())
				.map(ratings -> IntStream.rangeClosed(0, ratingsList.size() - 1)
						.allMatch(value -> checkRatings(ratingsList, value)))
				.orElse(false);
	}

	private static boolean checkRatings(List<Double> ratingsList, int i) {
		return mockedRatingList.get(i).equals(ratingsList.get(i));
	}

	private static boolean checkNamesIfEqual(List<String> trainerNamesList) {
		return TrainerRatingServiceTest.mockedTrainerNamesList.size() == trainerNamesList.size() &&
				IntStream.rangeClosed(0, trainerNamesList.size() - 1)
						.allMatch(value -> checkTrainerNames(
								trainerNamesList,
								value));
	}

	private static boolean checkTrainerNames(List<String> trainerNamesList, int i) {
		return TrainerRatingServiceTest.mockedTrainerNamesList.get(i).equals(trainerNamesList.get(i));
	}

	private void checkEitherLeft(boolean ifLeft,
								 TrainerErrorMessageType errorMessageType,
								 ErrorMsg errorMsgEitherLeft) {
		assertTrue(ifLeft);
		assertEquals(errorMessageType.toString(), errorMsgEitherLeft.getMsg());
	}

	private void verifyFindDocumentByEmail() {
		verify(trainerDao, times(1))
				.findByEmail(TRAINER_DOCUMENT_EMAIL);
		verify(trainerDao)
				.findByEmail(stringArgumentCaptor.capture());
	}

	private void verifySaveTrainerDocument() {
		verify(ratingTrainerDao, times(1))
				.save(any());
		verify(ratingTrainerDao)
				.save(ratingTrainerDocumentArgumentCaptor.capture());
	}
}