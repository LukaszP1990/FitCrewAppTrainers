package com.fitcrew.FitCrewAppTrainers.service.trainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.ClientResponsesDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.ClientResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainingResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerCapabilitiesServiceTest {

	private static List<TrainingDto> mockedTrainerDtos = TrainingResourceMockUtil.getListOfTrainings();
	private static TrainingDto mockedUpdatedTrainerDto = TrainingResourceMockUtil.updateTrainingDto(1);
	private static List<String> mockedClientNames = ClientResourceMockUtil.getListOfClients();
	private static final TrainerEntity mockedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";

	@Mock
	private FeignTrainingService feignTrainingService;

	@Mock
	private TrainerDao trainerDao;

	@InjectMocks
	private TrainerCapabilitiesService trainerCapabilitiesService;

	@Test
	void shouldGetClientsWhoGetTrainingFromTrainer() {

		when(feignTrainingService.getTrainerTrainings(anyString()))
				.thenReturn(mockedTrainerDtos);

		when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
				.thenReturn(mockedClientNames);

		Either<ErrorMsg, List<String>> clients =
				trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(TRAINER_EMAIL);
		assertNotNull(clients);
		assertAll(() -> {
			assertTrue(clients.isRight());
			assertEquals(9, clients.get().size());
		});
	}

	@Test
	void shouldNotGetClientsWhoGetTrainingFromTrainer() {

		Either<ErrorMsg, List<String>> noClients =
				trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(TRAINER_EMAIL);
		assertNotNull(noClients);

		checkEitherLeft(
				true,
				"No trainings found",
				noClients.getLeft());
	}

	@Test
	void shouldGetClientResponses() {


	}

	@Test
	void shouldNotGetClientResponses() {
	}

	@Test
	void shouldGetBasicInformationsAboutTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);

		Either<ErrorMsg, TrainerDto> basicInformationsAboutTrainer =
				trainerCapabilitiesService.getBasicInformationsAboutTrainer(TRAINER_EMAIL);

		assertNotNull(basicInformationsAboutTrainer);

		assertAll(() -> {
			assertTrue(basicInformationsAboutTrainer.isRight());
			assertEquals("firstName", basicInformationsAboutTrainer.get().getFirstName());
			assertEquals("lastName", basicInformationsAboutTrainer.get().getLastName());
			assertEquals("01.01.1990", basicInformationsAboutTrainer.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					basicInformationsAboutTrainer.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, basicInformationsAboutTrainer.get().getEmail());
			assertEquals("501928341", basicInformationsAboutTrainer.get().getPhone());
		});
	}

	@Test
	void shouldNotGetBasicInformationsAboutTrainer() {

		Either<ErrorMsg, TrainerDto> noBasicInformationsAboutTrainer =
				trainerCapabilitiesService.getBasicInformationsAboutTrainer(TRAINER_EMAIL);

		assertNotNull(noBasicInformationsAboutTrainer);

		checkEitherLeft(
				true,
				"Trainer not found by email address",
				noBasicInformationsAboutTrainer.getLeft());
	}

	@Test
	void shouldGetTrainerTrainings() {

		when(feignTrainingService.getTrainerTrainings(anyString()))
				.thenReturn(mockedTrainerDtos);

		Either<ErrorMsg, List<TrainingDto>> trainerTrainings =
				trainerCapabilitiesService.getTrainerTrainings(TRAINER_EMAIL);

		assertNotNull(trainerTrainings);

		assertAll(() -> {
			assertTrue(trainerTrainings.isRight());
			assertEquals(3, trainerTrainings.get().size());
		});

	}

	@Test
	void shouldNotGetTrainerTrainings() {

		Either<ErrorMsg, List<TrainingDto>> noTrainerTrainings =
				trainerCapabilitiesService.getTrainerTrainings(TRAINER_EMAIL);

		assertNotNull(noTrainerTrainings);

		checkEitherLeft(
				true,
				"No trainings to return",
				noTrainerTrainings.getLeft());
	}

	@Test
	void shouldCreateTraining() {
	}

	@Test
	void shouldNotCreateTraining() {
	}

	@Test
	void shouldDeleteTraining() {
	}

	@Test
	void shouldNotDeleteTraining() {
	}

	@Test
	void shouldUpdateTraining() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);

		when(feignTrainingService.updateTraining(any(), anyString()))
				.thenReturn(mockedUpdatedTrainerDto);

		Either<ErrorMsg, TrainingDto> updatedTraining =
				trainerCapabilitiesService.updateTraining(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		assertNotNull(updatedTraining);
		assertAll(() -> {
			assertTrue(updatedTraining.isRight());
			assertEquals("default name 1", updatedTraining.get().getTrainingName());
			assertEquals("some training", updatedTraining.get().getTraining());
			assertEquals(TRAINER_EMAIL, updatedTraining.get().getTrainerEmail());
		});
	}

	@Test
	void shouldNotUpdateTraining() {

		Either<ErrorMsg, TrainingDto> noUpdatedTraining =
				trainerCapabilitiesService.updateTraining(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		assertNotNull(noUpdatedTraining);

		checkEitherLeft(
				true,
				"No training updated",
				noUpdatedTraining.getLeft());
	}

	@Test
	void shouldSelectTrainingToSend() {
	}

	@Test
	void shouldNotSelectTrainingToSend() {
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