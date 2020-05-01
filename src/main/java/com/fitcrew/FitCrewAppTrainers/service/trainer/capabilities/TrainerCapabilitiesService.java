package com.fitcrew.FitCrewAppTrainers.service.trainer.capabilities;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.converter.TrainingDtoTrainingModelConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class TrainerCapabilitiesService {

    private final FeignTrainingService feignTrainingService;
    private final TrainerDao trainerDao;
    private final TrainingDtoTrainingModelConverter trainingConverter;
    private final TrainerDocumentTrainerModelConverter trainerConverter;

    public TrainerCapabilitiesService(FeignTrainingService feignTrainingService,
                                      TrainerDao trainerDao,
                                      TrainingDtoTrainingModelConverter trainingConverter,
                                      TrainerDocumentTrainerModelConverter trainerConverter) {
        this.feignTrainingService = feignTrainingService;
        this.trainerDao = trainerDao;
        this.trainingConverter = trainingConverter;
        this.trainerConverter = trainerConverter;
    }

    public Either<ErrorMsg, List<String>> getClientsWhoGetTrainingFromTrainer(String trainingName) {
        return Optional.ofNullable(feignTrainingService.clientsWhoBoughtTraining(trainingName))
                .map(Either::<ErrorMsg, List<String>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_CLIENT_BOUGHT_TRAINING.toString())));
    }

    public Either<ErrorMsg, TrainerModel> getBasicInformationAboutTrainer(String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .map(this::checkEitherResponseForTrainer)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    public Either<ErrorMsg, List<TrainingModel>> getTrainerTrainings(String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerDocument -> feignTrainingService.getTrainerTrainings(trainerEmail))
                .map(this::checkEitherResponseForTrainings)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAININGS.toString())));
    }

    public Either<ErrorMsg, TrainingModel> createTraining(TrainingDto trainingDto) {
        return trainerDao.findByEmail(trainingDto.getTrainerEmail())
                .map(trainerDocument -> trainingConverter.trainingDtoToTrainingModel(trainingDto))
                .map(feignTrainingService::createTraining)
                .map(this::checkEitherResponseForTraining)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_CREATED.toString())));
    }

    public Either<ErrorMsg, TrainingModel> deleteTraining(String trainerEmail,
                                                          String trainingName) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerDocument -> feignTrainingService.deleteTraining(trainerEmail, trainingName))
                .map(Either::<ErrorMsg, TrainingModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_DELETED.toString())));
    }

    public Either<ErrorMsg, TrainingModel> updateTraining(TrainingDto trainingDto,
                                                          String trainerEmail) {
        return trainerDao.findByEmail(trainingDto.getTrainerEmail())
                .map(trainerDocument -> trainingConverter.trainingDtoToTrainingModel(trainingDto))
                .map(trainingModel -> feignTrainingService.updateTraining(trainingModel, trainerEmail))
                .map(this::checkEitherResponseForTraining)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_UPDATED.toString())));
    }

    public Either<ErrorMsg, TrainingModel> selectTrainingToSend(String trainerEmail,
                                                                String trainingName) {
        return trainerDao.findByEmail(trainerEmail)
                .filter(trainerDocument -> Objects.nonNull(trainerEmail))
                .map(trainerDocument -> feignTrainingService.selectTraining(trainerEmail, trainingName))
                .map(this::checkEitherResponseForTraining)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_SELECTED.toString())));
    }

    private Either<ErrorMsg, TrainingModel> checkEitherResponseForTraining(TrainingModel training) {
        return Optional.ofNullable(training)
                .map(Either::<ErrorMsg, TrainingModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }

    private Either<ErrorMsg, List<TrainingModel>> checkEitherResponseForTrainings(List<TrainingModel> training) {
        return Optional.ofNullable(training)
                .map(Either::<ErrorMsg, List<TrainingModel>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }

    private Either<ErrorMsg, TrainerModel> checkEitherResponseForTrainer(TrainerModel trainer) {
        return Optional.ofNullable(trainer)
                .map(Either::<ErrorMsg, TrainerModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }
}

