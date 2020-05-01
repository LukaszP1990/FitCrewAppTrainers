package com.fitcrew.FitCrewAppTrainers.service.admin;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainerAdminService {

    private final TrainerDao trainerDao;
    private final TrainerDocumentTrainerModelConverter trainerConverter;

    TrainerAdminService(TrainerDao trainerDao,
                        TrainerDocumentTrainerModelConverter trainerConverter) {
        this.trainerDao = trainerDao;
        this.trainerConverter = trainerConverter;
    }

    public Either<ErrorMsg, List<TrainerModel>> getTrainers() {
        return Optional.ofNullable(trainerDao.findAll())
                .map(this::mapTrainerDocumentsToModels)
                .map(Either::<ErrorMsg, List<TrainerModel>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    Either<ErrorMsg, TrainerModel> deleteTrainer(String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(this::prepareSuccessfulClientDeleting)
                .map(this::checkEitherResponseForTrainer)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER_DELETED.toString())));
    }

    Either<ErrorMsg, TrainerModel> updateTrainer(TrainerDto trainerDto,
                                                 String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerDocument -> prepareTrainingUpdate(trainerDto, trainerDocument))
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER_UPDATED.toString())));
    }

    public Either<ErrorMsg, TrainerModel> getTrainer(String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .map(this::checkEitherResponseForTrainer)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    private TrainerModel prepareSuccessfulClientDeleting(TrainerDocument trainerDocument) {
        trainerDao.delete(trainerDocument);
        return trainerConverter.trainerDocumentToTrainerModel(trainerDocument);
    }

    private Either<ErrorMsg, TrainerModel> prepareTrainingUpdate(TrainerDto trainerDto,
                                                                 TrainerDocument trainerDocument) {
        var trainerDocumentAfterUpdate = setNewValuesForTrainer(trainerDto, trainerDocument);
        trainerDao.save(trainerDocumentAfterUpdate);

        return checkEitherResponseForTrainer(
                trainerConverter.trainerDocumentToTrainerModel(trainerDocumentAfterUpdate)
        );
    }

    private TrainerDocument setNewValuesForTrainer(TrainerDto trainerDto,
                                                   TrainerDocument trainerDocument) {
        trainerDocument.setTrainerId(trainerDto.getTrainerId());
        trainerDocument.setDateOfBirth(trainerDto.getDateOfBirth());
        trainerDocument.setEncryptedPassword(trainerDto.getEncryptedPassword());
        trainerDocument.setFirstName(trainerDto.getFirstName());
        trainerDocument.setLastName(trainerDto.getLastName());
        trainerDocument.setPhone(trainerDto.getPhone());
        return trainerDocument;
    }


    private Either<ErrorMsg, TrainerModel> checkEitherResponseForTrainer(TrainerModel trainer) {
        return Optional.ofNullable(trainer)
                .map(Either::<ErrorMsg, TrainerModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }

    private List<TrainerModel> mapTrainerDocumentsToModels(List<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .collect(Collectors.toList());
    }
}
