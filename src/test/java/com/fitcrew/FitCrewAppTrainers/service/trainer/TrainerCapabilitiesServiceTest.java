package com.fitcrew.FitCrewAppTrainers.service.trainer;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.ClientResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainingResourceMockUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerCapabilitiesServiceTest {

    private static List<TrainingDto> mockedTrainerDtos = TrainingResourceMockUtil.getListOfTrainings();
    private static TrainingDto mockedUpdatedTrainerDto = TrainingResourceMockUtil.updateTrainingDto(1);
    private static TrainingDto mockedCreatedTrainerDto = TrainingResourceMockUtil.getTraining(1);
    private static List<String> mockedClientNames = ClientResourceMockUtil.getListOfClients();
    private static final TrainerEntity mockedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
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
    private ArgumentCaptor<TrainingDto> trainingDtoArgumentCaptor;

    @Mock
    private FeignTrainingService feignTrainingService;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerCapabilitiesService trainerCapabilitiesService;

//    @Test
//    void shouldGetClientsWhoGetTrainingFromTrainer() {
//
//        when(feignTrainingService.getTrainerTrainings(anyString()))
//                .thenReturn(mockedTrainerDtos);
//
//        when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
//                .thenReturn(mockedClientNames);
//
//        Either<ErrorMsg, List<String>> clients =
//                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(TRAINER_EMAIL);
//
//        verifyGetTrainerTrainings();
//
//        verifyClientsWhoBoughtTraining();
//
//        assertNotNull(clients);
//        assertAll(() -> {
//            assertTrue(clients.isRight());
//            assertEquals(9, clients.get().size());
//        });
//    }
//
//    @Test
//    void shouldNotGetClientsWhoGetTrainingFromTrainer() {
//
//        Either<ErrorMsg, List<String>> noClients =
//                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(TRAINER_EMAIL);
//        assertNotNull(noClients);
//
//        checkEitherLeft(
//                true,
//                "No trainings found",
//                noClients.getLeft());
//    }

    @Test
    void shouldGetClientResponses() {

    }

    @Test
    void shouldNotGetClientResponses() {
    }

    @Test
    void shouldGetBasicInformationsAboutTrainer() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerEntity));

        Either<ErrorMsg, TrainerDto> basicInformationsAboutTrainer =
                trainerCapabilitiesService.getBasicInformationsAboutTrainer(TRAINER_EMAIL);

        verifyFindEntityByEmail();

        assertNotNull(basicInformationsAboutTrainer);
        assertAll(() -> {
            assertTrue(basicInformationsAboutTrainer.isRight());
            assertEquals(TRAINER_FIRST_NAME, basicInformationsAboutTrainer.get().getFirstName());
            assertEquals(TRAINER_LAST_NAME, basicInformationsAboutTrainer.get().getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, basicInformationsAboutTrainer.get().getDateOfBirth());
            assertEquals(
                    ENCRYPTED_PASSWORD,
                    basicInformationsAboutTrainer.get().getEncryptedPassword()
            );
            assertEquals(TRAINER_EMAIL, basicInformationsAboutTrainer.get().getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, basicInformationsAboutTrainer.get().getPhone());
        });
    }

    @Test
    void shouldNotGetBasicInformationsAboutTrainer() {

        Either<ErrorMsg, TrainerDto> noBasicInformationsAboutTrainer =
                trainerCapabilitiesService.getBasicInformationsAboutTrainer(TRAINER_EMAIL);

        assertNotNull(noBasicInformationsAboutTrainer);
        checkEitherLeft(noBasicInformationsAboutTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER, noBasicInformationsAboutTrainer.getLeft());
    }

    @Test
    void shouldGetTrainerTrainings() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerEntity));

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
        checkEitherLeft(noTrainerTrainings.isLeft(), TrainerErrorMessageType.NO_TRAININGS, noTrainerTrainings.getLeft());
    }

    @Test
    void shouldCreateTraining() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerEntity));

        when(feignTrainingService.createTraining(any()))
                .thenReturn(mockedCreatedTrainerDto);

        Either<ErrorMsg, TrainingDto> training =
                trainerCapabilitiesService.createTraining(mockedCreatedTrainerDto);

        verifyFindEntityByEmail();
        verifyCreateTraining();
        assertNotNull(training);
        checkAssertionsForTraining(training);
    }

    @Test
    void shouldNotCreateTraining() {

        Either<ErrorMsg, TrainingDto> noTraining =
                trainerCapabilitiesService.createTraining(mockedCreatedTrainerDto);

        assertNotNull(noTraining);
        checkEitherLeft(noTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_CREATED, noTraining.getLeft());
    }

    @Test
    void shouldDeleteTraining() {
        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerEntity));

        when(feignTrainingService.deleteTraining(anyString(), anyString()))
                .thenReturn(mockedCreatedTrainerDto);

        Either<ErrorMsg, TrainingDto> deletedTraining =
                trainerCapabilitiesService.deleteTraining(TRAINER_EMAIL, TRAINING_NAME);

        verifyDeleteTraining();
        assertNotNull(deletedTraining);
        checkAssertionsForTraining(deletedTraining);
    }


    @Test
    void shouldNotDeleteTraining() {

        Either<ErrorMsg, TrainingDto> noDeletedTraining =
                trainerCapabilitiesService.deleteTraining(TRAINER_EMAIL, TRAINING_NAME);

        assertNotNull(noDeletedTraining);
        checkEitherLeft(noDeletedTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_DELETED, noDeletedTraining.getLeft());
    }

    @Test
    void shouldUpdateTraining() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerEntity));

        when(feignTrainingService.updateTraining(any(), anyString()))
                .thenReturn(mockedUpdatedTrainerDto);

        Either<ErrorMsg, TrainingDto> updatedTraining =
                trainerCapabilitiesService.updateTraining(mockedUpdatedTrainerDto, TRAINER_EMAIL);

        verifyFindEntityByEmail();
        verifyUpdateTraining();
        assertNotNull(updatedTraining);
        checkAssertionsForTraining(updatedTraining);
    }

    @Test
    void shouldNotUpdateTraining() {

        Either<ErrorMsg, TrainingDto> noUpdatedTraining =
                trainerCapabilitiesService.updateTraining(mockedUpdatedTrainerDto, TRAINER_EMAIL);

        assertNotNull(noUpdatedTraining);
        checkEitherLeft(noUpdatedTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_UPDATED, noUpdatedTraining.getLeft());
    }

    @Test
    void shouldSelectTrainingToSend() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerEntity));

        when(feignTrainingService.selectTraining(anyString(), anyString()))
                .thenReturn(mockedCreatedTrainerDto);

        Either<ErrorMsg, TrainingDto> selectedTraining =
                trainerCapabilitiesService.selectTrainingToSend(TRAINER_EMAIL, TRAINING_NAME);

        verifyFindEntityByEmail();
        assertNotNull(selectedTraining);
        checkAssertionsForTraining(selectedTraining);

    }

    @Test
    void shouldNotSelectTrainingToSend() {

        Either<ErrorMsg, TrainingDto> noSelectedTraining =
                trainerCapabilitiesService.selectTrainingToSend(TRAINER_EMAIL, TRAINING_NAME);

        assertNotNull(noSelectedTraining);
        checkEitherLeft(noSelectedTraining.isLeft(), TrainerErrorMessageType.NO_TRAINING_SELECTED, noSelectedTraining.getLeft());
    }

    private void checkAssertionsForTraining(Either<ErrorMsg, TrainingDto> selectedTraining) {
        assertAll(() -> {
            assertTrue(selectedTraining.isRight());
            assertEquals(TRAINING_NAME, selectedTraining.get().getTrainingName());
            assertEquals("some training", selectedTraining.get().getTraining());
            assertEquals(TRAINER_EMAIL, selectedTraining.get().getTrainerEmail());
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
                .updateTraining(mockedUpdatedTrainerDto, TRAINER_EMAIL);

        verify(feignTrainingService)
                .updateTraining(
                        trainingDtoArgumentCaptor.capture(),
                        argumentCaptorString.capture()
                );
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