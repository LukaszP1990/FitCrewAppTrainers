package com.fitcrew.FitCrewAppTrainers.resource.converter;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainingDtoTrainingModelConverter;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.util.TrainingResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDtoTrainingModelConverterTest {

    private static TrainingDto trainingDto = TrainingResourceMockUtil.getTrainingDto(1);
    private TrainingDtoTrainingModelConverter trainingConverter = Mappers.getMapper(TrainingDtoTrainingModelConverter.class);
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String DESCRIPTION = "default description";
    private static String NAME = "default name 1";
    private static String TRAINING = "default description";

    @Test
    void shouldConvertTrainerDtoToTrainerModel() {
        TrainingModel trainingModel = trainingConverter.trainingDtoToTrainingModel(trainingDto);
        assertNotNull(trainingModel);
        assertAll(() -> {
            assertEquals(NAME, trainingModel.getTrainingName());
            assertEquals(DESCRIPTION, trainingModel.getDescription());
            assertEquals(TRAINER_EMAIL, trainingModel.getTrainerEmail());
            assertEquals(TRAINING, trainingModel.getTraining());
        });
    }
}
