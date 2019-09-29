package com.fitcrew.FitCrewAppTrainers.service.admin;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainerAdminService {

    private final TrainerDao trainerDao;
    private String SUCCESSFULLY_MAPPING = "Trainer object mapped successfully and send to admin web service {}";
    private String NOT_SUCCESSFULLY_MAPPING = "Trainer object not mapped successfully";

    public TrainerAdminService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Either<ErrorMsg, List<TrainerDto>> getTrainers() {
        Iterable<TrainerEntity> trainerEntities = trainerDao.findAll();
        ArrayList<TrainerEntity> trainerEntitiesList = Lists.newArrayList(trainerEntities);

        if (!trainerEntitiesList.isEmpty()) {

            ModelMapper modelMapper = prepareModelMapperForExistingTrainer();
            List<TrainerDto> trainersToReturn = mapTrainerEntitiesToDtoObjects(trainerEntitiesList, modelMapper);
            return checkEitherResponseForTrainers(trainersToReturn);
        } else {
            log.debug("No trainer found");
            return Either.left(new ErrorMsg("No trainer found"));
        }
    }

    public Either<ErrorMsg, TrainerDto> deleteTrainer(String trainerEmail) {

        TrainerEntity trainerToDelete = trainerDao.findByEmail(trainerEmail);

        if (trainerToDelete != null) {
            ModelMapper modelMapper = prepareModelMapperForExistingTrainer();
            log.debug("Trainer to delete {}", trainerToDelete);
            trainerDao.delete(trainerToDelete);
            TrainerDto trainerToReturn = modelMapper.map(trainerToDelete, TrainerDto.class);

            return checkEitherResponseForTrainer(
                    trainerToReturn,
                    SUCCESSFULLY_MAPPING,
                    NOT_SUCCESSFULLY_MAPPING);
        } else {
            log.debug("No trainer deleted");
            return Either.left(new ErrorMsg("No trainer deleted"));
        }
    }

    public Either<ErrorMsg, TrainerDto> updateTrainer(TrainerDto trainerDto,
                                                      String trainerEmail) {

        TrainerEntity trainerToUpdate = trainerDao.findByEmail(trainerEmail);

        if (trainerToUpdate != null) {
            log.debug("Trainer to update {}", trainerToUpdate);
            return prepareTrainingUpdate(trainerDto, trainerToUpdate);
        } else {
            log.debug("No trainer updated");
            return Either.left(new ErrorMsg("No client updated"));
        }
    }

    public Either<ErrorMsg, TrainerDto> getTrainer(String trainerEmail) {

        TrainerEntity trainerEntity = trainerDao.findByEmail(trainerEmail);

        if (trainerEntity != null) {
            ModelMapper modelMapper = prepareModelMapperForExistingTrainer();
            log.debug("Trainer to return {}", trainerEntity);

            TrainerDto trainer = modelMapper.map(trainerEntity, TrainerDto.class);

            return checkEitherResponseForTrainer(
                    trainer,
                    SUCCESSFULLY_MAPPING,
                    NOT_SUCCESSFULLY_MAPPING);
        } else {
            return Either.left(new ErrorMsg("No training found"));
        }
    }

    private Either<ErrorMsg, TrainerDto> prepareTrainingUpdate(TrainerDto trainerDto,
                                                               TrainerEntity foundTrainerEntityByEmail) {
        ModelMapper modelMapper = prepareModelMapperForExistingTrainer();

        setNewValuesForClient(trainerDto, foundTrainerEntityByEmail);

        log.debug("Training updated {}", foundTrainerEntityByEmail);
        TrainerDto trainerToReturn = modelMapper.map(foundTrainerEntityByEmail, TrainerDto.class);

        return checkEitherResponseForTrainer(
                trainerToReturn,
                SUCCESSFULLY_MAPPING,
                NOT_SUCCESSFULLY_MAPPING);
    }

    private void setNewValuesForClient(TrainerDto trainerDto,
                                       TrainerEntity foundTrainerEntityByEmail) {
        foundTrainerEntityByEmail.setTrainerId(trainerDto.getTrainerId());
        foundTrainerEntityByEmail.setDateOfBirth(trainerDto.getDateOfBirth());
        foundTrainerEntityByEmail.setEncryptedPassword(trainerDto.getEncryptedPassword());
        foundTrainerEntityByEmail.setFirstName(trainerDto.getFirstName());
        foundTrainerEntityByEmail.setLastName(trainerDto.getLastName());
        foundTrainerEntityByEmail.setPhone(trainerDto.getPhone());
    }

    private ModelMapper prepareModelMapperForExistingTrainer() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    private Either<ErrorMsg, List<TrainerDto>> checkEitherResponseForTrainers(List<TrainerDto> trainersToReturn) {
        if (!trainersToReturn.isEmpty()) {
            log.debug("Trainer to return: {}", trainersToReturn);
            return Either.right(trainersToReturn);
        } else {
            log.debug("No trainer to return");
            return Either.left(new ErrorMsg("No trainer to return"));
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

    private List<TrainerDto> mapTrainerEntitiesToDtoObjects(ArrayList<TrainerEntity> trainerEntitiesList, ModelMapper modelMapper) {
        return trainerEntitiesList.stream()
                .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerDto.class))
                .collect(Collectors.toList());
    }
}
