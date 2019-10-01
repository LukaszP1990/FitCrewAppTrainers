package com.fitcrew.FitCrewAppTrainers.service.admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerAdminServiceTest {

	private final static List<TrainerEntity> mockedTrainerEntities = TrainerResourceMockUtil.createTrainerEntities();
	private final static TrainerEntity mockedCreatedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private final static TrainerDto mockedUpdatedTrainerDto = TrainerResourceMockUtil.updateTrainerDto();
	private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";

	@Mock
	private TrainerDao trainerDao;

	@InjectMocks
	private TrainerAdminService trainerAdminService;

	@Test
	void shouldGetTrainers() {

		when(trainerDao.findAll())
				.thenReturn(mockedTrainerEntities);

		Either<ErrorMsg, List<TrainerDto>> trainers = trainerAdminService.getTrainers();
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
		checkEitherLeft(
				true,
				"No trainer found",
				noTrainers.getLeft());
	}

	@Test
	void shouldDeleteTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedCreatedTrainerEntity);

		Either<ErrorMsg, TrainerDto> deletedTrainer =
				trainerAdminService.deleteTrainer(TRAINER_EMAIL);

		assertNotNull(deletedTrainer);
		assertAll(() -> {
			assertTrue(deletedTrainer.isRight());
			assertEquals("updatedFirstName", deletedTrainer.get().getFirstName());
			assertEquals("updatedLastName", deletedTrainer.get().getLastName());
			assertEquals("02.02.1990", deletedTrainer.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					deletedTrainer.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, deletedTrainer.get().getEmail());
			assertEquals("501928342", deletedTrainer.get().getPhone());
		});
	}

	@Test
	void shouldNotDeleteTrainer() {

		Either<ErrorMsg, TrainerDto> noDeletedTrainer =
				trainerAdminService.getTrainer(TRAINER_EMAIL);

		assertNotNull(noDeletedTrainer);
		checkEitherLeft(
				true,
				"No trainer found",
				noDeletedTrainer.getLeft());
	}

	@Test
	void shouldUpdateTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedCreatedTrainerEntity);

		Either<ErrorMsg, TrainerDto> updatedTrainer =
				trainerAdminService.updateTrainer(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		assertNotNull(updatedTrainer);
		assertAll(() -> {
			assertTrue(updatedTrainer.isRight());
			assertEquals("updatedFirstName", updatedTrainer.get().getFirstName());
			assertEquals("updatedLastName", updatedTrainer.get().getLastName());
			assertEquals("02.02.1990", updatedTrainer.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					updatedTrainer.get().getEncryptedPassword()
			);
			assertEquals("501928342", updatedTrainer.get().getPhone());
		});
	}

	@Test
	void shouldNotUpdateTrainer() {

		Either<ErrorMsg, TrainerDto> noUpdatedTrainer =
				trainerAdminService.updateTrainer(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		assertNotNull(noUpdatedTrainer);
		checkEitherLeft(
				true,
				"No trainer updated",
				noUpdatedTrainer.getLeft());
	}

	@Test
	void shouldGetTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedCreatedTrainerEntity);

		Either<ErrorMsg, TrainerDto> trainer =
				trainerAdminService.getTrainer(TRAINER_EMAIL);

		assertNotNull(trainer);
		assertAll(() -> {
			assertTrue(trainer.isRight());
			assertEquals("firstName", trainer.get().getFirstName());
			assertEquals("lastName", trainer.get().getLastName());
			assertEquals("01.01.1990", trainer.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					trainer.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, trainer.get().getEmail());
			assertEquals("501928341", trainer.get().getPhone());
		});
	}

	@Test
	void shouldNotGetTrainer() {

		Either<ErrorMsg, TrainerDto> noTrainer =
				trainerAdminService.getTrainer(TRAINER_EMAIL);

		assertNotNull(noTrainer);

		checkEitherLeft(
				true,
				"No trainer found",
				noTrainer.getLeft());
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