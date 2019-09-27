package com.fitcrew.FitCrewAppTrainers.service.trainer;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.ClientResponsesDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignTrainingService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public Either<ErrorMsg, List<String>> getClientsWhoGetTrainingFromTrainer(String trainerEmail) {

        Either<ErrorMsg, List<TrainingDto>> trainerTrainings = getTrainerTrainings(trainerEmail);

        if (trainerTrainings.isRight()) {
            List<String> allTrainerTrainings = getAllTrainings(trainerTrainings);
            return checkEitherResponseForTrainers(allTrainerTrainings);
        } else {
            return Either.left(new ErrorMsg("No trainings found"));
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

    public Either<ErrorMsg, TrainingDto> createTraining(TrainingDto trainingDto,
                                                        String trainerEmail) {

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

    public Either<ErrorMsg, TrainingDto> deleteTraining(String trainerEmail,
                                                        String trainingName) {

        TrainingDto deletedTraining = feignTrainingService.deleteTraining(trainerEmail, trainingName);
        if (deletedTraining != null) {
            log.debug("Training deleted {}", deletedTraining);
            return Either.right(deletedTraining);
        } else {
            log.debug("No training deleted");
            return Either.left(new ErrorMsg("No training deleted"));
        }
    }

    public Either<ErrorMsg, TrainingDto> updateTraining(TrainingDto trainingDto,
                                                        String trainerEmail) {

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

    public Either<ErrorMsg, TrainingDto> selectTrainingToSend(String trainerEmail,
                                                              String trainingName) {
        TrainerEntity trainerEntity = trainerDao.findByEmail(trainerEmail);
        if (trainerEmail != null) {
            log.debug("Trainer who selected training to send to the client {}", trainerEntity);
            TrainingDto selectedTraining = feignTrainingService.selectTraining(trainerEmail, trainingName);

            return checkEitherResponseForTraining(selectedTraining,
                    "Training selected {}",
                    "No training selected");
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

    private Either<ErrorMsg, List<String>> checkEitherResponseForTrainers(List<String> allTrainerTrainings) {
        if (!allTrainerTrainings.isEmpty()) {
            log.debug("List of trainings {}", allTrainerTrainings);
            List<String> allClients = getAllClients(allTrainerTrainings);
            return checkEitherResponseForClients(allClients);
        } else {
            log.debug("No trainings found");
            return Either.left(new ErrorMsg("No trainings found"));
        }
    }

    private Either<ErrorMsg, List<String>> checkEitherResponseForClients(List<String> allClients) {
        if (!allClients.isEmpty()) {
            log.debug("List of clients who has bought trainings from trainer {}", allClients);
            return Either.right(allClients);
        } else {
            log.debug("No client has bought trainings from trainer");
            return Either.left(new ErrorMsg("No client has bought trainings from trainer"));
        }
    }

    private List<String> getAllClients(List<String> allTrainerTrainings) {
        return allTrainerTrainings.stream()
                .distinct()
                .flatMap(training -> feignTrainingService.clientsWhoBoughtTraining(training).stream())
                .collect(Collectors.toList());
    }

    private List<String> getAllTrainings(Either<ErrorMsg, List<TrainingDto>> trainerTrainings) {
        return trainerTrainings.get().stream()
                .map(TrainingDto::getTrainingName)
                .collect(Collectors.toList());
    }
}

