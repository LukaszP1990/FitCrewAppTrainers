package com.fitcrew.FitCrewAppTrainers.resource.trainer;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resource.AbstractRestResourceTest;
import com.fitcrew.FitCrewAppTrainers.util.ClientResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainingResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class TrainerCapabilitiesResourceTest extends AbstractRestResourceTest {

    private static List<TrainingModel> mockedModelTrainings = TrainingResourceMockUtil.getListOfModelTrainings();
    private static TrainingModel mockedTrainingModel = TrainingResourceMockUtil.getTrainingModel(1);
    private static List<String> mockedClientNames = ClientResourceMockUtil.getListOfClients();
    private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();

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
                .thenReturn(mockedModelTrainings);
    }

    @Test
    void shouldCreateTraining() {

        Mockito.when(feignTrainingService.createTraining(any()))
                .thenReturn(mockedTrainingModel);
    }

    @Test
    void shouldDeleteTraining() {

        Mockito.when(feignTrainingService.deleteTraining(anyString(), anyString()))
                .thenReturn(mockedTrainingModel);
    }

    @Test
    void shouldUpdateTraining() {

        Mockito.when(feignTrainingService.updateTraining(any(), anyString()))
                .thenReturn(mockedTrainingModel);
    }

    @Test
    void shouldSelectTrainingToSend() {

        Mockito.when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));
        Mockito.when(feignTrainingService.selectTraining(anyString(), anyString()))
                .thenReturn(mockedTrainingModel);
    }

    @Test
    void shouldClientsWhoBoughtTraining() {

        Mockito.when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
                .thenReturn(mockedClientNames);
    }
}