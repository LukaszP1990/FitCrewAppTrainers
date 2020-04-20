package com.fitcrew.FitCrewAppTrainers.util;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainingResourceMockUtil {

    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String DESCRIPTION = "default description";
    private static String NAME = "default name";
    private static String TRAINING = "default description";

    public static List<TrainingModel> getListOfModelTrainings() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(TrainingResourceMockUtil::prepareTrainingModelData)
                .collect(Collectors.toList());
    }

    public static TrainingDto getTrainingDto(int value) {
        return prepareTrainingDtoData(value);
    }

    public static TrainingDto getNotValidTrainingDto() {
        return prepareNotValidTrainingDtoData();
    }

    public static TrainingModel getTrainingModel(int value) {
        return prepareTrainingModelData(value);
    }

    private static TrainingDto prepareTrainingDtoData(int value) {
        return TrainingDto.builder()
                .description(DESCRIPTION)
                .trainerEmail(TRAINER_EMAIL)
                .trainingName(NAME + " " + value)
                .training(TRAINING)
                .build();
    }

    private static TrainingDto prepareNotValidTrainingDtoData() {
        return TrainingDto.builder()
                .description(DESCRIPTION)
                .build();
    }

    private static TrainingModel prepareTrainingModelData(int value) {
        return TrainingModel.builder()
                .description(DESCRIPTION)
                .trainerEmail(TRAINER_EMAIL)
                .trainingName(NAME + " " + value)
                .training(TRAINING)
                .build();
    }
}
