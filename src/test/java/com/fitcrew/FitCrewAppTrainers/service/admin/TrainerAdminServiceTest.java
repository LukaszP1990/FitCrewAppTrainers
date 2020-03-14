package com.fitcrew.FitCrewAppTrainers.service.admin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverterImpl;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerAdminServiceTest {

	private final static List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
	private final static TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
	private final static TrainerDto mockedUpdatedTrainerDto = TrainerResourceMockUtil.updateTrainerDto();
	private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
	private static String TRAINER_FIRST_NAME = "firstName";
	private static String TRAINER_LAST_NAME = "lastName";
	private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
	private static String TRAINER_PHONE_NUMBER = "501928341";

	@Captor
	private ArgumentCaptor<String> stringArgumentCaptor;

	private TrainerDao trainerDao = Mockito.mock(TrainerDao.class);
	private TrainerDocumentTrainerDtoConverter trainerConverter = new TrainerDocumentTrainerDtoConverterImpl();

	private TrainerAdminService trainerAdminService = new TrainerAdminService(trainerDao, trainerConverter);

	@Test
	void shouldGetTrainers() {

		when(trainerDao.findAll())
				.thenReturn(mockedTrainerDocuments);

		Either<ErrorMsg, List<TrainerDto>> trainers = trainerAdminService.getTrainers();

		verify(trainerDao, times(1)).findAll();
		assertNotNull(trainers);
		assertAll(() -> {
			assertTrue(trainers.isRight());
			assertEquals(3, trainers.get().size());
		});
	}

	@Test
	void shouldNotGetTrainers() {

		Either<ErrorMsg, List<TrainerDto>> noTrainers = trainerAdminService.getTrainers();

		assertNotNull(noTrainers);
		checkEitherLeft(noTrainers.isLeft(), TrainerErrorMessageType.NO_TRAINER, noTrainers.getLeft());
	}

	@Test
	void shouldDeleteTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerDocument));

		Either<ErrorMsg, TrainerDto> deletedTrainer =
				trainerAdminService.deleteTrainer(TRAINER_EMAIL);

		verifyFindDocumentByEmail();
		checkAssertions(deletedTrainer);
	}

	@Test
	void shouldNotDeleteTrainer() {

		Either<ErrorMsg, TrainerDto> noDeletedTrainer =
				trainerAdminService.deleteTrainer(TRAINER_EMAIL);

		assertNotNull(noDeletedTrainer);
		checkEitherLeft(noDeletedTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER_DELETED, noDeletedTrainer.getLeft());
	}

	@Test
	void shouldUpdateTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerDocument));

		Either<ErrorMsg, TrainerDto> updatedTrainer =
				trainerAdminService.updateTrainer(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		verifyFindDocumentByEmail();
		checkAssertions(updatedTrainer);
	}

	@Test
	void shouldNotUpdateTrainer() {

		Either<ErrorMsg, TrainerDto> noUpdatedTrainer =
				trainerAdminService.updateTrainer(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		assertNotNull(noUpdatedTrainer);
		checkEitherLeft(noUpdatedTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER_UPDATED, noUpdatedTrainer.getLeft());
	}

	@Test
	void shouldGetTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerDocument));

		Either<ErrorMsg, TrainerDto> trainer =
				trainerAdminService.getTrainer(TRAINER_EMAIL);

		verifyFindDocumentByEmail();
		checkAssertions(trainer);
	}

	@Test
	void shouldNotGetTrainer() {

		Either<ErrorMsg, TrainerDto> noTrainer =
				trainerAdminService.getTrainer(TRAINER_EMAIL);

		assertNotNull(noTrainer);
		checkEitherLeft(noTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER, noTrainer.getLeft());
	}

	private void checkEitherLeft(boolean ifLeft,
								 TrainerErrorMessageType errorMessageType,
								 ErrorMsg errorMsgEitherLeft) {
		assertTrue(ifLeft);
		assertEquals(errorMessageType.toString(), errorMsgEitherLeft.getMsg());
	}


	private void verifyFindDocumentByEmail() {
		verify(trainerDao, times(1))
				.findByEmail(TRAINER_EMAIL);
		verify(trainerDao)
				.findByEmail(stringArgumentCaptor.capture());
	}

	private void checkAssertions(Either<ErrorMsg, TrainerDto> trainerDto) {
		assertNotNull(trainerDto);
		assertAll(() -> {
			assertTrue(trainerDto.isRight());
			assertEquals(TRAINER_FIRST_NAME, trainerDto.get().getFirstName());
			assertEquals(TRAINER_LAST_NAME, trainerDto.get().getLastName());
			assertEquals(TRAINER_DATE_OF_BIRTH, trainerDto.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					trainerDto.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, trainerDto.get().getEmail());
			assertEquals(TRAINER_PHONE_NUMBER, trainerDto.get().getPhone());
		});
	}
}