package com.fitcrew.FitCrewAppTrainers.resource.trainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resource.AbstractRestResourceTest;
import com.fitcrew.FitCrewAppTrainers.util.ClientResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainingResourceMockUtil;

class TrainerCapabilitiesResourceTest extends AbstractRestResourceTest {

	private static List<TrainingDto> mockedTrainerDtos = TrainingResourceMockUtil.getListOfTrainings();
	private static TrainingDto mockedCreatedTrainingDto = TrainingResourceMockUtil.getTraining(1);
	private static List<String> mockedClientNames = ClientResourceMockUtil.getListOfClients();
	private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
	private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
	private static String TRAINER_FIRST_NAME = "firstName";
	private static String TRAINER_LAST_NAME = "lastName";
	private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
	private static String TRAINER_PHONE_NUMBER = "501928341";
	private static String TRAINING_NAME = "default name 1";

	@MockBean
	private FeignTrainingService feignTrainingService;
	@MockBean
	private TrainerDao trainerDao;

	@Test
	void shouldGetBasicInformationAboutTrainer() {

		Mockito.when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerDocument));
	}

	@Test
	void shouldGetTrainerTrainings() {

		Mockito.when(feignTrainingService.getTrainerTrainings(anyString()))
				.thenReturn(mockedTrainerDtos);
	}

	@Test
	void shouldCreateTraining() {

		Mockito.when(feignTrainingService.createTraining(any()))
				.thenReturn(mockedCreatedTrainingDto);
	}

	@Test
	void shouldDeleteTraining() {

		Mockito.when(feignTrainingService.deleteTraining(anyString(), anyString()))
				.thenReturn(mockedCreatedTrainingDto);
	}

	@Test
	void shouldUpdateTraining() {

		Mockito.when(feignTrainingService.updateTraining(any(), anyString()))
				.thenReturn(mockedCreatedTrainingDto);
	}

	@Test
	void shouldSelectTrainingToSend() {

		Mockito.when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerDocument));
		Mockito.when(feignTrainingService.selectTraining(anyString(), anyString()))
				.thenReturn(mockedCreatedTrainingDto);
	}

	@Test
	void shouldClientsWhoBoughtTraining() {

		Mockito.when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
				.thenReturn(mockedClientNames);
	}
}