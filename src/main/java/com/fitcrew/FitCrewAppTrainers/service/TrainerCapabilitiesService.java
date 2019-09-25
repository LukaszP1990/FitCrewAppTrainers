package com.fitcrew.FitCrewAppTrainers.service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.ClientDto;
import com.fitcrew.FitCrewAppTrainers.dto.ClientResponsesDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignClientService;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerCapabilitiesService {

	private final FeignClientService feignClientService;
	private final FeignTrainingService feignTrainingService;
	private final TrainerDao trainerDao;

	public TrainerCapabilitiesService(FeignClientService feignClientService,
									  FeignTrainingService feignTrainingService,
									  TrainerDao trainerDao) {
		this.feignClientService = feignClientService;
		this.feignTrainingService = feignTrainingService;
		this.trainerDao = trainerDao;
	}

	public Either<ErrorMsg, List<ClientDto>> getClientsWhoGetTrainingFromTrainer(String trainerName) {
		List<ClientDto> clientsWhoGetTrainingFromTrainer =
				feignClientService.getClientsWhoGetTrainingFromTrainer(trainerName);

		if (!clientsWhoGetTrainingFromTrainer.isEmpty()) {
			log.debug("List of clients {}", clientsWhoGetTrainingFromTrainer);
			return Either.right(clientsWhoGetTrainingFromTrainer);
		} else {
			log.debug("No client found");
			return Either.left(new ErrorMsg("No client to return"));
		}
	}

	public Either<ErrorMsg, List<ClientResponsesDto>> getClientResponses(String trainerName) {

		//responses from clients to single trainer
		return null;
	}

	public Either<ErrorMsg, TrainerDto> getBasicInformationsAboutTrainer(String trainerEmail) {

		ModelMapper modelMapper = prepareModelMapperForExistingTraining();

		TrainerEntity trainerEntityFoundByEmail = trainerDao.findByEmail(trainerEmail);

		if (trainerEntityFoundByEmail != null) {
			log.debug("Trainer found by email address {}", trainerEntityFoundByEmail);
			TrainerDto trainerToReturn = modelMapper.map(trainerEntityFoundByEmail, TrainerDto.class);

			return checkEitherResponseForTrainer(trainerToReturn,
					"Trainer object mapped successfully and send to trainer web service {}",
					"Trainer not mapped successfully");
		} else {
			log.debug("Trainer not found by email address");
			return Either.left(new ErrorMsg("Trainer not found by email address"));
		}
	}

	public Either<ErrorMsg, List<TrainingDto>> getTrainerTrainings(String trainerEmail) {

		List<TrainingDto> trainerTrainings = feignTrainingService.getTrainerTrainings(trainerEmail);
		if (!trainerTrainings.isEmpty()) {
			log.debug("List of trainings {}", trainerTrainings);
			return Either.right(trainerTrainings);
		} else {
			log.debug("No trainings found");
			return Either.left(new ErrorMsg("No trainings to return"));
		}
	}

	public Either<ErrorMsg, TrainingDto> createTraining(TrainingDto trainingDto, String trainerEmail) {

		TrainerEntity trainerEntity = trainerDao.findByEmail(trainerEmail);

		if (trainerEmail != null) {
			log.debug("Trainer who create new training {}", trainerEntity);
			TrainingDto createdTraining = feignTrainingService.createTraining(trainingDto);

			return checkEitherResponseForTraining(createdTraining,
					"Training created {}",
					"No training created");
		} else {
			log.debug("No trainer found");
			return Either.left(new ErrorMsg("No trainer found"));
		}

	}

	public Either<ErrorMsg, TrainingDto> deleteTraining(String trainerEmail, String trainingName) {

		TrainingDto deletedTraining = feignTrainingService.deleteTraining(trainerEmail, trainingName);
		if (deletedTraining != null) {
			log.debug("Training deleted {}", deletedTraining);
			return Either.right(deletedTraining);
		} else {
			log.debug("No training deleted");
			return Either.left(new ErrorMsg("No training deleted"));
		}
	}

	public Either<ErrorMsg, TrainingDto> updateTraining(TrainingDto trainingDto, String trainerEmail) {

		TrainerEntity trainerEntity = trainerDao.findByEmail(trainerEmail);
		if (trainerEmail != null) {
			log.debug("Trainer who update training {}", trainerEntity);
			TrainingDto updatedTraining = feignTrainingService.updateTraining(trainingDto, trainerEmail);

			return checkEitherResponseForTraining(updatedTraining,
					"Training updated {}",
					"No training updated");
		} else {
			log.debug("No trainer found");
			return Either.left(new ErrorMsg("No trainer found"));
		}
	}

	private Either<ErrorMsg, TrainingDto> checkEitherResponseForTraining(TrainingDto training,
																		 String eitherRightMessage,
																		 String eitherLeftMessage) {
		if (training != null) {
			log.debug(eitherRightMessage, training);
			return Either.right(training);
		} else {
			log.debug(eitherLeftMessage);
			return Either.left(new ErrorMsg(eitherLeftMessage));
		}
	}

	private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer,
																	   String eitherRightMessage,
																	   String eitherLeftMessage) {
		if (trainer != null) {
			log.debug(eitherRightMessage, trainer);
			return Either.right(trainer);
		} else {
			log.debug(eitherLeftMessage);
			return Either.left(new ErrorMsg(eitherLeftMessage));
		}
	}

	private ModelMapper prepareModelMapperForExistingTraining() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
}

