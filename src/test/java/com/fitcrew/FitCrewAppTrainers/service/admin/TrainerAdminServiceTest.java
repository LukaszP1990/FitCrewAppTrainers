package com.fitcrew.FitCrewAppTrainers.service.admin;

import static org.junit.jupiter.api.Assertions.*;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerAdminServiceTest {

	private final static List<TrainerEntity> mockedTrainerEntities = TrainerResourceMockUtil.createTrainerEntities();
	private final static TrainerEntity mockedCreatedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private final static TrainerDto mockedUpdatedTrainerDto = TrainerResourceMockUtil.updateTrainerDto();
	private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
	private static String TRAINER_FIRST_NAME = "firstName";
	private static String TRAINER_LAST_NAME = "lastName";
	private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
	private static String TRAINER_PHONE_NUMBER = "501928341";

	@Captor
	private ArgumentCaptor<String> stringArgumentCaptor;

	@Mock
	private TrainerDao trainerDao;

	@InjectMocks
	private TrainerAdminService trainerAdminService;

	@Test
	void shouldGetTrainers() {

		when(trainerDao.findAll())
				.thenReturn(mockedTrainerEntities);

		Either<ErrorMsg, List<TrainerDto>> trainers = trainerAdminService.getTrainers();

		verify(trainerDao,times(1)).findAll();
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
				.thenReturn(Optional.of(mockedCreatedTrainerEntity));

		Either<ErrorMsg, TrainerDto> deletedTrainer =
				trainerAdminService.deleteTrainer(TRAINER_EMAIL);

		verifyFindEntityByEmail();
		assertNotNull(deletedTrainer);
		assertAll(() -> {
			assertTrue(deletedTrainer.isRight());
			assertEquals(TRAINER_FIRST_NAME, deletedTrainer.get().getFirstName());
			assertEquals(TRAINER_LAST_NAME, deletedTrainer.get().getLastName());
			assertEquals(TRAINER_DATE_OF_BIRTH, deletedTrainer.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					deletedTrainer.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, deletedTrainer.get().getEmail());
			assertEquals(TRAINER_PHONE_NUMBER, deletedTrainer.get().getPhone());
		});
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
				.thenReturn(Optional.of(mockedCreatedTrainerEntity));

		Either<ErrorMsg, TrainerDto> updatedTrainer =
				trainerAdminService.updateTrainer(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		verifyFindEntityByEmail();
		assertNotNull(updatedTrainer);
		assertAll(() -> {
			assertTrue(updatedTrainer.isRight());
			assertEquals(TRAINER_FIRST_NAME, updatedTrainer.get().getFirstName());
			assertEquals(TRAINER_LAST_NAME, updatedTrainer.get().getLastName());
			assertEquals(TRAINER_DATE_OF_BIRTH, updatedTrainer.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					updatedTrainer.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_PHONE_NUMBER, updatedTrainer.get().getPhone());
		});
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
				.thenReturn(Optional.of(mockedCreatedTrainerEntity));

		Either<ErrorMsg, TrainerDto> trainer =
				trainerAdminService.getTrainer(TRAINER_EMAIL);

		verifyFindEntityByEmail();
		assertNotNull(trainer);
		assertAll(() -> {
			assertTrue(trainer.isRight());
			assertEquals(TRAINER_FIRST_NAME, trainer.get().getFirstName());
			assertEquals(TRAINER_LAST_NAME, trainer.get().getLastName());
			assertEquals(TRAINER_DATE_OF_BIRTH, trainer.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					trainer.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, trainer.get().getEmail());
			assertEquals(TRAINER_PHONE_NUMBER, trainer.get().getPhone());
		});
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


	private void verifyFindEntityByEmail() {
		verify(trainerDao, times(1))
				.findByEmail(TRAINER_EMAIL);
		verify(trainerDao)
				.findByEmail(stringArgumentCaptor.capture());
	}
}