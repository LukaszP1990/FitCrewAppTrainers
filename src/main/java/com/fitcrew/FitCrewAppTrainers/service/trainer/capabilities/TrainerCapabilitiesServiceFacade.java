package com.fitcrew.FitCrewAppTrainers.service.trainer.capabilities;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;

import java.util.List;

public interface TrainerCapabilitiesServiceFacade {
    Either<ErrorMsg, List<String>> getClientsWhoGetTrainingFromTrainer(String trainingName);

    Either<ErrorMsg, TrainerModel> getBasicInformationAboutTrainer(String trainerEmail);

    Either<ErrorMsg, List<TrainingModel>> getTrainerTrainings(String trainerEmail);

    Either<ErrorMsg, TrainingModel> createTraining(TrainingDto trainingDto);

    Either<ErrorMsg, TrainingModel> deleteTraining(String trainerEmail, String trainingName);

    Either<ErrorMsg, TrainingModel> updateTraining(TrainingDto trainingDto, String trainerEmail);

    Either<ErrorMsg, TrainingModel> selectTrainingToSend(String trainerEmail, String trainingName);
}
