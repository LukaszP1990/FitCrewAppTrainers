package com.fitcrew.FitCrewAppTrainers.service.trainer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerCapabilitiesService {

	private final FeignTrainingService feignTrainingService;
	private final TrainerDao trainerDao;
	private final TrainerDocumentTrainerDtoConverter trainerConverter;

	TrainerCapabilitiesService(FeignTrainingService feignTrainingService,
							   TrainerDao trainerDao, TrainerDocumentTrainerDtoConverter trainerConverter) {
		this.feignTrainingService = feignTrainingService;
		this.trainerDao = trainerDao;
		this.trainerConverter = trainerConverter;
	}

	public Either<ErrorMsg, List<String>> getClientsWhoGetTrainingFromTrainer(String trainingName) {
		return Optional.ofNullable(feignTrainingService.clientsWhoBoughtTraining(trainingName))
				.filter(clientNames -> !clientNames.isEmpty())
				.map(Either::<ErrorMsg, List<String>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_CLIENT_BOUGHT_TRAINING.toString())));
	}

	public Either<ErrorMsg, TrainerDto> getBasicInformationAboutTrainer(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerConverter::trainerDocumentToTrainerDto)
				.map(this::checkEitherResponseForTrainer)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	public Either<ErrorMsg, List<TrainingDto>> getTrainerTrainings(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerDocument -> feignTrainingService.getTrainerTrainings(trainerEmail))
				.map(this::checkEitherResponseForTrainings)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAININGS.toString())));
	}

	public Either<ErrorMsg, TrainingDto> createTraining(TrainingDto trainingDto) {
		return trainerDao.findByEmail(trainingDto.getTrainerEmail())
				.map(trainerDto -> feignTrainingService.createTraining(trainingDto))
				.map(this::checkEitherResponseForTraining)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_CREATED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> deleteTraining(String trainerEmail,
														String trainingName) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerDocument -> feignTrainingService.deleteTraining(trainerEmail, trainingName))
				.map(Either::<ErrorMsg, TrainingDto>right)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_DELETED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> updateTraining(TrainingDto trainingDto,
														String trainerEmail) {
		return trainerDao.findByEmail(trainingDto.getTrainerEmail())
				.map(trainerDocument -> feignTrainingService.updateTraining(trainingDto, trainerEmail))
				.map(this::checkEitherResponseForTraining)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_UPDATED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> selectTrainingToSend(String trainerEmail,
															  String trainingName) {
		return trainerDao.findByEmail(trainerEmail)
				.filter(trainerDocument -> Objects.nonNull(trainerEmail))
				.map(trainerDocument -> feignTrainingService.selectTraining(trainerEmail, trainingName))
				.map(this::checkEitherResponseForTraining)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_SELECTED.toString())));
	}

	private Either<ErrorMsg, TrainingDto> checkEitherResponseForTraining(TrainingDto training) {
		return Optional.ofNullable(training)
				.map(Either::<ErrorMsg, TrainingDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private Either<ErrorMsg, List<TrainingDto>> checkEitherResponseForTrainings(List<TrainingDto> training) {
		return Optional.ofNullable(training)
				.map(Either::<ErrorMsg, List<TrainingDto>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer) {
		return Optional.ofNullable(trainer)
				.map(Either::<ErrorMsg, TrainerDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}
}

