package com.fitcrew.FitCrewAppTrainers.service.trainer.capabilities;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerCapabilitiesServiceFacadeImpl implements TrainerCapabilitiesServiceFacade {

    private final TrainerCapabilitiesService trainerCapabilitiesService;

    public TrainerCapabilitiesServiceFacadeImpl(TrainerCapabilitiesService trainerCapabilitiesService) {
        this.trainerCapabilitiesService = trainerCapabilitiesService;
    }

    @Override
    public Either<ErrorMsg, List<String>> getClientsWhoGetTrainingFromTrainer(String trainingName) {
        return trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(trainingName);
    }

    @Override
    public Either<ErrorMsg, TrainerModel> getBasicInformationAboutTrainer(String trainerEmail) {
        return trainerCapabilitiesService.getBasicInformationAboutTrainer(trainerEmail);
    }

    @Override
    public Either<ErrorMsg, List<TrainingModel>> getTrainerTrainings(String trainerEmail) {
        return trainerCapabilitiesService.getTrainerTrainings(trainerEmail);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> createTraining(TrainingDto trainingDto) {
        return trainerCapabilitiesService.createTraining(trainingDto);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> deleteTraining(String trainerEmail, String trainingName) {
        return trainerCapabilitiesService.deleteTraining(trainerEmail, trainingName);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> updateTraining(TrainingDto trainingDto, String trainerEmail) {
        return trainerCapabilitiesService.updateTraining(trainingDto, trainerEmail);
    }

    @Override
    public Either<ErrorMsg, TrainingModel> selectTrainingToSend(String trainerEmail, String trainingName) {
        return trainerCapabilitiesService.selectTrainingToSend(trainerEmail, trainingName);
    }
}
