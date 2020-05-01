package com.fitcrew.FitCrewAppTrainers.service.trainer;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.converter.*;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.service.trainer.capabilities.TrainerCapabilitiesService;
import com.fitcrew.FitCrewAppTrainers.util.ClientResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainingResourceMockUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerCapabilitiesServiceTest {

    private static List<TrainingModel> mockedTrainerModels = TrainingResourceMockUtil.getListOfModelTrainings();
    private static TrainingModel mockedTrainingModel = TrainingResourceMockUtil.getTrainingModel(1);
    private static TrainingDto mockedTrainingDto = TrainingResourceMockUtil.getTrainingDto(1);
    private static List<String> mockedClientNames = ClientResourceMockUtil.getListOfClients();
    private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    private static String TRAINER_PHONE_NUMBER = "501928341";
    private static String TRAINING_NAME = "default name 1";

    @Captor
    private ArgumentCaptor<String> argumentCaptorString;

    @Captor
    private ArgumentCaptor<TrainingModel> trainingModelArgumentCaptor;

    private FeignTrainingService feignTrainingService = Mockito.mock(FeignTrainingService.class);
    private TrainerDao trainerDao = Mockito.mock(TrainerDao.class);
    private TrainingDtoTrainingModelConverter trainingConverter = new TrainingDtoTrainingModelConverterImpl();
    private TrainerDocumentTrainerModelConverter trainerConverter = new TrainerDocumentTrainerModelConverterImpl();

    private TrainerCapabilitiesService trainerCapabilitiesService =
            new TrainerCapabilitiesService(feignTrainingService, trainerDao, trainingConverter, trainerConverter);

    @Test
    void shouldGetClientsWhoGetTrainingFromTrainer() {

        when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
                .thenReturn(mockedClientNames);

        Either<ErrorMsg, List<String>> clients =
                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(TRAINING_NAME);

        verifyClientsWhoBoughtTraining();

        assertNotNull(clients);
        assertAll(() -> {
            assertTrue(clients.isRight());
            assertEquals(3, clients.get().size());
        });
    }

    @Test
    void shouldNotGetClientsWhoGetTrainingFromTrainer() {

        when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
                .thenReturn(null);

        Either<ErrorMsg, List<String>> noClients =
                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(TRAINING_NAME);
        assertNotNull(noClients);

        checkEitherLeft(
                true,
                TrainerErrorMessageType.NO_CLIENT_BOUGHT_TRAINING,
                noClients.getLeft());
    }

    @Test
    void shouldGetBasicInformationAboutTrainer() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        Either<ErrorMsg, TrainerModel> basicInformationAboutTrainer =
                trainerCapabilitiesService.getBasicInformationAboutTrainer(TRAINER_EMAIL);

        verifyFindEntityByEmail();

        assertNotNull(basicInformationAboutTrainer);
        assertAll(() -> {
            assertTrue(basicInformationAboutTrainer.isRight());
            assertEquals(TRAINER_FIRST_NAME, basicInformationAboutTrainer.get().getFirstName());
            assertEquals(TRAINER_LAST_NAME, basicInformationAboutTrainer.get().getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, basicInformationAboutTrainer.get().getDateOfBirth());
            assertEquals(
                    ENCRYPTED_PASSWORD,
                    basicInformationAboutTrainer.get().getEncryptedPassword()
            );
            assertEquals(TRAINER_EMAIL, basicInformationAboutTrainer.get().getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, basicInformationAboutTrainer.get().getPhone());
        });
    }

    @Test
    void shouldNotGetBasicInformationAboutTrainer() {

        Either<ErrorMsg, TrainerModel> noBasicInformationAboutTrainer =
                trainerCapabilitiesService.getBasicInformationAboutTrainer(TRAINER_EMAIL);

        assertNotNull(noBasicInformationAboutTrainer);
        checkEitherLeft(noBasicInformationAboutTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER, noBasicInformationAboutTrainer.getLeft());
    }

    @Test
    void shouldGetTrainerTrainings() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        when(feignTrainingService.getTrainerTrainings(anyString()))
                .thenReturn(mockedTrainerModels);

        Either<ErrorMsg, List<TrainingModel>> trainerTrainings =
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

        Either<ErrorMsg, List<TrainingModel>> noTrainerTrainings =
                trainerCapabilitiesService.getTrainerTrainings(TRAINER_EMAIL);

        assertNotNull(noTrainerTrainings);
        checkEitherLeft(noTrainerTrainings.isLeft(), TrainerErrorMessageType.NO_TRAININGS, noTrainerTrainings.getLeft());
    }

    @Test
    void shouldCreateTraining() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        when(feignTrainingService.createTraining(any()))
                .thenReturn(mockedTrainingModel);

        Either<ErrorMsg, TrainingModel> training =
                trainerCapabilitiesService.createTraining(mockedTrainingDto);

        verifyFindEntityByEmail();
        verify(feignTrainingService)
                .createTraining(trainingModelArgumentCaptor.capture());
        assertNotNull(training);
        checkAssertionsForTraining(training);
    }

    @Test
    void shouldNotCreateTraining() {

        Either<ErrorMsg, TrainingModel> noTraining =
                trainerCapabilitiesService.createTraining(mockedTrainingDto);

        assertNotNull(noTraining);
        checkEitherLeft(noTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_CREATED, noTraining.getLeft());
    }

    @Test
    void shouldDeleteTraining() {
        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        when(feignTrainingService.deleteTraining(anyString(), anyString()))
                .thenReturn(mockedTrainingModel);

        Either<ErrorMsg, TrainingModel> deletedTraining =
                trainerCapabilitiesService.deleteTraining(TRAINER_EMAIL, TRAINING_NAME);

        verifyDeleteTraining();
        assertNotNull(deletedTraining);
        checkAssertionsForTraining(deletedTraining);
    }


    @Test
    void shouldNotDeleteTraining() {

        Either<ErrorMsg, TrainingModel> noDeletedTraining =
                trainerCapabilitiesService.deleteTraining(TRAINER_EMAIL, TRAINING_NAME);

        assertNotNull(noDeletedTraining);
        checkEitherLeft(noDeletedTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_DELETED, noDeletedTraining.getLeft());
    }

    @Test
    void shouldUpdateTraining() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        when(feignTrainingService.updateTraining(any(), anyString()))
                .thenReturn(mockedTrainingModel);

        Either<ErrorMsg, TrainingModel> updatedTraining =
                trainerCapabilitiesService.updateTraining(mockedTrainingDto, TRAINER_EMAIL);

        verifyFindEntityByEmail();
        verify(feignTrainingService)
                .updateTraining(
                        trainingModelArgumentCaptor.capture(),
                        argumentCaptorString.capture()
                );
        assertNotNull(updatedTraining);
        checkAssertionsForTraining(updatedTraining);
    }

    @Test
    void shouldNotUpdateTraining() {

        Either<ErrorMsg, TrainingModel> noUpdatedTraining =
                trainerCapabilitiesService.updateTraining(mockedTrainingDto, TRAINER_EMAIL);

        assertNotNull(noUpdatedTraining);
        checkEitherLeft(noUpdatedTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_UPDATED, noUpdatedTraining.getLeft());
    }

    @Test
    void shouldSelectTrainingToSend() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        when(feignTrainingService.selectTraining(anyString(), anyString()))
                .thenReturn(mockedTrainingModel);

        Either<ErrorMsg, TrainingModel> selectedTraining =
                trainerCapabilitiesService.selectTrainingToSend(TRAINER_EMAIL, TRAINING_NAME);

        verifyFindEntityByEmail();
        assertNotNull(selectedTraining);
        checkAssertionsForTraining(selectedTraining);

    }

    @Test
    void shouldNotSelectTrainingToSend() {

        Either<ErrorMsg, TrainingModel> noSelectedTraining =
                trainerCapabilitiesService.selectTrainingToSend(TRAINER_EMAIL, TRAINING_NAME);

        assertNotNull(noSelectedTraining);
        checkEitherLeft(noSelectedTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_SELECTED, noSelectedTraining.getLeft());
    }

    private void checkAssertionsForTraining(Either<ErrorMsg, TrainingModel> selectedTraining) {
        assertAll(() -> {
            assertTrue(selectedTraining.isRight());
            assertEquals(TRAINING_NAME, selectedTraining.get().getTrainingName());
            assertEquals("default description", selectedTraining.get().getTraining());
            assertEquals(TRAINER_EMAIL, selectedTraining.get().getTrainerEmail());
        });
    }

    private void verifyClientsWhoBoughtTraining() {
        verify(feignTrainingService, times(1))
                .clientsWhoBoughtTraining(anyString());

        verify(feignTrainingService, times(1))
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

    private void verifyDeleteTraining() {
        verify(feignTrainingService, times(1))
                .deleteTraining(TRAINER_EMAIL, TRAINING_NAME);

        verify(feignTrainingService)
                .deleteTraining(argumentCaptorString.capture(), argumentCaptorString.capture());
    }

    private void checkEitherLeft(boolean ifLeft,
                                 TrainerErrorMessageType errorMessageType,
                                 ErrorMsg errorMsgEitherLeft) {
        assertTrue(ifLeft);
        assertEquals(errorMessageType.toString(), errorMsgEitherLeft.getMsg());
    }
}