package com.fitcrew.FitCrewAppTrainers.service.admin;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainerAdminService {

    private final TrainerDao trainerDao;

    public TrainerAdminService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Either<ErrorMsg, List<TrainerDto>> getTrainers() {
        ModelMapper modelMapper = prepareModelMapperForExistingTrainer();

        return Optional.ofNullable(trainerDao.findAll())
                .map(Lists::newArrayList)
                .filter(trainerEntitiesList -> !trainerEntitiesList.isEmpty())
                .map(trainerEntitiesList -> mapTrainerEntitiesToDtoObjects(trainerEntitiesList, modelMapper))
                .map(this::checkEitherResponseForTrainers)
                .orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    public Either<ErrorMsg, TrainerDto> deleteTrainer(String trainerEmail) {
        ModelMapper modelMapper = prepareModelMapperForExistingTrainer();

        return trainerDao.findByEmail(trainerEmail)
                .map(trainerEntity -> prepareSuccesfulClientDeleting(modelMapper, trainerEntity))
                .map(this::checkEitherResponseForTrainer)
                .orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER_DELETED.toString())));
    }

    public Either<ErrorMsg, TrainerDto> updateTrainer(TrainerDto trainerDto,
                                                      String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerEntity -> prepareTrainingUpdate(trainerDto, trainerEntity))
                .orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER_UPDATED.toString())));
    }

    public Either<ErrorMsg, TrainerDto> getTrainer(String trainerEmail) {
        ModelMapper modelMapper = prepareModelMapperForExistingTrainer();

        return trainerDao.findByEmail(trainerEmail)
                .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerDto.class))
                .map(this::checkEitherResponseForTrainer)
                .orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    private TrainerDto prepareSuccesfulClientDeleting(ModelMapper modelMapper, TrainerEntity trainerEntity) {
        trainerDao.delete(trainerEntity);
        return modelMapper.map(trainerEntity, TrainerDto.class);
    }

    private Either<ErrorMsg, TrainerDto> prepareTrainingUpdate(TrainerDto trainerDto,
                                                               TrainerEntity foundTrainerEntityByEmail) {
        ModelMapper modelMapper = prepareModelMapperForExistingTrainer();
        setNewValuesForClient(trainerDto, foundTrainerEntityByEmail);
        TrainerDto trainerToReturn = modelMapper.map(foundTrainerEntityByEmail, TrainerDto.class);

        return checkEitherResponseForTrainer(trainerToReturn);
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

    private Either<ErrorMsg, List<TrainerDto>> checkEitherResponseForTrainers(List<TrainerDto> trainersToReturn) {
        return Optional.ofNullable(trainersToReturn)
                .filter(trainers -> !trainers.isEmpty())
                .map(Either::<ErrorMsg, List<TrainerDto>>right)
                .orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_CLIENT_FOUND.toString())));
    }

    private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer) {
        return Optional.ofNullable(trainer)
                .map(Either::<ErrorMsg, TrainerDto>right)
                .orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }

    private List<TrainerDto> mapTrainerEntitiesToDtoObjects(ArrayList<TrainerEntity> trainerEntitiesList, ModelMapper modelMapper) {
        return trainerEntitiesList.stream()
                .map(trainerEntity -> modelMapper.map(trainerEntity, TrainerDto.class))
                .collect(Collectors.toList());
    }

    private ModelMapper prepareModelMapperForExistingTrainer() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
