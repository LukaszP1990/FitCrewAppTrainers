package com.fitcrew.FitCrewAppTrainers.service.trainer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
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

	public TrainerCapabilitiesService(FeignTrainingService feignTrainingService,
									  TrainerDao trainerDao) {
		this.feignTrainingService = feignTrainingService;
		this.trainerDao = trainerDao;
	}

//	public Either<ErrorMsg, List<String>> getClientsWhoGetTrainingFromTrainer(String trainerEmail) {
//
//		Either<ErrorMsg, List<TrainingDto>> trainerTrainings = getTrainerTrainings(trainerEmail);
//
//		trainerDao.findByEmail(trainerEmail)
//				.map(trainerEntity -> trainerEntity)
//				.map(trainerEntity -> feignTrainingService.getTrainerTrainings(trainerEmail))
//				.map(this::checkEitherResponseForTrainings)
//				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAININGS.toString())));
//	}

//	public Either<ErrorMsg, List<ClientResponsesDto>> getClientResponses(String trainerName) {
//
//		//responses from clients to single trainer
//		return null;
//	}

	public Either<ErrorMsg, TrainerDto> getBasicInformationsAboutTrainer(String trainerEmail) {
		ModelMapper modelMapper = prepareModelMapperForExistingTraining();

		return trainerDao.findByEmail(trainerEmail)
				.map(trainerEntity -> modelMapper.map(trainerEntity, TrainerDto.class))
				.map(this::checkEitherResponseForTrainer)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	public Either<ErrorMsg, List<TrainingDto>> getTrainerTrainings(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerEntity -> feignTrainingService.getTrainerTrainings(trainerEmail))
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
				.map(trainerEntity -> feignTrainingService.deleteTraining(trainerEmail, trainingName))
				.map(Either::<ErrorMsg, TrainingDto>right)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_DELETED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> updateTraining(TrainingDto trainingDto,
														String trainerEmail) {
		return trainerDao.findByEmail(trainingDto.getTrainerEmail())
				.map(trainerEntity -> feignTrainingService.updateTraining(trainingDto, trainerEmail))
				.map(this::checkEitherResponseForTraining)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_UPDATED.toString())));
	}

	public Either<ErrorMsg, TrainingDto> selectTrainingToSend(String trainerEmail,
															  String trainingName) {
		return trainerDao.findByEmail(trainerEmail)
				.filter(trainerEntity -> Objects.nonNull(trainerEmail))
				.map(trainerEntity -> feignTrainingService.selectTraining(trainerEmail, trainingName))
				.map(this::checkEitherResponseForTraining)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_SELECTED.toString())));
	}

	private Either<ErrorMsg, TrainingDto> checkEitherResponseForTraining(TrainingDto training) {
		return Optional.ofNullable(training).map(Either::<ErrorMsg, TrainingDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private Either<ErrorMsg, List<TrainingDto>> checkEitherResponseForTrainings(List<TrainingDto> training) {
		return Optional.ofNullable(training).map(Either::<ErrorMsg, List<TrainingDto>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer) {
		return Optional.ofNullable(trainer).map(Either::<ErrorMsg, TrainerDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private ModelMapper prepareModelMapperForExistingTraining() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
}

