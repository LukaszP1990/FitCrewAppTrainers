package com.fitcrew.FitCrewAppTrainers.service.trainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
	private static TrainingDto mockedCreatedTrainerDto = TrainingResourceMockUtil.getTraining(1);
	private static List<String> mockedClientNames = ClientResourceMockUtil.getListOfClients();
	private static final TrainerEntity mockedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";

	@Captor
	private ArgumentCaptor<String> argumentCaptorString;

	@Captor
	private ArgumentCaptor<TrainingDto> trainingDtoArgumentCaptor;

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

		verifyGetTrainerTrainings();

		verifyClientsWhoBoughtTraining();

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

		verifyFindEntityByEmail();

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

		verifyGetTrainerTrainings();

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

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);

		when(feignTrainingService.createTraining(any()))
				.thenReturn(mockedCreatedTrainerDto);

		Either<ErrorMsg, TrainingDto> training =
				trainerCapabilitiesService.createTraining(mockedCreatedTrainerDto, TRAINER_EMAIL);

		verifyFindEntityByEmail();

		verifyCreateTraining();

		assertNotNull(training);

		assertAll(() -> {
			assertTrue(training.isRight());
			assertEquals("default name 1", training.get().getTrainingName());
			assertEquals("some training", training.get().getTraining());
			assertEquals(TRAINER_EMAIL, training.get().getTrainerEmail());
		});
	}

	@Test
	void shouldNotCreateTraining() {

		Either<ErrorMsg, TrainingDto> noTraining =
				trainerCapabilitiesService.createTraining(mockedCreatedTrainerDto, TRAINER_EMAIL);

		assertNotNull(noTraining);

		checkEitherLeft(
				true,
				"No training created",
				noTraining.getLeft());
	}

	@Test
	void shouldDeleteTraining() {
	}

	@Test
	void shouldNotDeleteTraining() {

		Either<ErrorMsg, TrainingDto> noDeletedTraining =
				trainerCapabilitiesService.deleteTraining(TRAINER_EMAIL, "default name 1");

		assertNotNull(noDeletedTraining);

		checkEitherLeft(
				true,
				"No training deleted",
				noDeletedTraining.getLeft());
	}

	@Test
	void shouldUpdateTraining() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);

		when(feignTrainingService.updateTraining(any(), anyString()))
				.thenReturn(mockedUpdatedTrainerDto);

		Either<ErrorMsg, TrainingDto> updatedTraining =
				trainerCapabilitiesService.updateTraining(mockedUpdatedTrainerDto, TRAINER_EMAIL);

		verifyFindEntityByEmail();

		verifyUpdateTraining();

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

		Either<ErrorMsg, TrainingDto> noSelectedTraining =
				trainerCapabilitiesService.selectTrainingToSend(TRAINER_EMAIL, "default name 1");

		assertNotNull(noSelectedTraining);

		checkEitherLeft(
				true,
				"No training selected",
				noSelectedTraining.getLeft());
	}

	private void checkEitherLeft(boolean value,
								 String message,
								 ErrorMsg errorMsg) {
		assertAll(() -> {
			assertTrue(value);
			assertEquals(message, errorMsg.getMsg());
		});
	}

	private void verifyClientsWhoBoughtTraining() {
		verify(feignTrainingService, times(3))
				.clientsWhoBoughtTraining(anyString());

		verify(feignTrainingService, times(3))
				.clientsWhoBoughtTraining(argumentCaptorString.capture());
	}

	private void verifyGetTrainerTrainings() {
		verify(feignTrainingService, times(1))
				.getTrainerTrainings(anyString());

		verify(feignTrainingService)
				.getTrainerTrainings(argumentCaptorString.capture());
	}

	private void verifyFindEntityByEmail() {
		verify(trainerDao, times(1))
				.findByEmail(TRAINER_EMAIL);

		verify(trainerDao)
				.findByEmail(argumentCaptorString.capture());
	}

	private void verifyCreateTraining() {
		verify(feignTrainingService, times(1))
				.createTraining(mockedCreatedTrainerDto);

		verify(feignTrainingService)
				.createTraining(trainingDtoArgumentCaptor.capture());
	}

	private void verifyUpdateTraining() {
		verify(feignTrainingService, times(1))
				.updateTraining(mockedUpdatedTrainerDto, anyString());

		verify(feignTrainingService)
				.updateTraining(
						trainingDtoArgumentCaptor.capture(),
						argumentCaptorString.capture()
				);
	}
}