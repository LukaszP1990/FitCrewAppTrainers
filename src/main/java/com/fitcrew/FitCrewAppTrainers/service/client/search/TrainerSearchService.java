package com.fitcrew.FitCrewAppTrainers.service.client.search;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
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
public class TrainerSearchService {

    private final TrainerDao trainerDao;
    private final TrainerDocumentTrainerModelConverter trainerModelConverter;

    public TrainerSearchService(TrainerDao trainerDao,
                                TrainerDocumentTrainerModelConverter trainerModelConverter) {
        this.trainerDao = trainerDao;
        this.trainerModelConverter = trainerModelConverter;
    }

    public Either<ErrorMsg, List<TrainerModel>> getTrainers() {
        return Optional.ofNullable(trainerDao.findAll())
                .filter(trainerDocuments -> !trainerDocuments.isEmpty())
                .map(this::mapTrainerDocumentToModel)
                .map(this::checkEitherResponseForTrainers)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINERS.toString())));
    }

    public Either<ErrorMsg, TrainerModel> getTrainer(String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerModelConverter::trainerDocumentToTrainerModel)
                .map(this::checkEitherResponseForTrainer)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    private List<TrainerModel> mapTrainerDocumentToModel(List<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .map(trainerModelConverter::trainerDocumentToTrainerModel)
                .collect(Collectors.toList());
    }

    private Either<ErrorMsg, List<TrainerModel>> checkEitherResponseForTrainers(List<TrainerModel> trainers) {
        return Optional.ofNullable(trainers)
                .filter(trainersList -> !trainersList.isEmpty())
                .map(Either::<ErrorMsg, List<TrainerModel>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }

    private Either<ErrorMsg, TrainerModel> checkEitherResponseForTrainer(TrainerModel trainer) {
        return Optional.ofNullable(trainer)
                .map(Either::<ErrorMsg, TrainerModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }
}
