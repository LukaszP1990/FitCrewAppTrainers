package com.fitcrew.FitCrewAppTrainers.service.trainer.create;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TrainerCreateService {

    private final TrainerDao trainerDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TrainerDocumentTrainerDtoConverter trainerDocumentDtoConverter;
    private final TrainerDocumentTrainerModelConverter trainerDocumentModelConverter;

    public TrainerCreateService(TrainerDao trainerDao,
                                BCryptPasswordEncoder bCryptPasswordEncoder,
                                TrainerDocumentTrainerDtoConverter trainerDocumentDtoConverter,
                                TrainerDocumentTrainerModelConverter trainerDocumentModelConverter) {
        this.trainerDao = trainerDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.trainerDocumentDtoConverter = trainerDocumentDtoConverter;
        this.trainerDocumentModelConverter = trainerDocumentModelConverter;
    }

    public Either<ErrorMsg, TrainerModel> createTrainer(TrainerDto trainerDto) {
        return Optional.of(trainerDto)
                .map(this::setPredefinedData)
                .map(trainerDocumentDtoConverter::trainerDtoToTrainerDocument)
                .map(trainerDao::save)
                .map(trainerDocumentModelConverter::trainerDocumentToTrainerModel)
                .map(Either::<ErrorMsg, TrainerModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_CREATED.toString())));
    }

    private TrainerDto setPredefinedData(TrainerDto trainerDto) {
        trainerDto.setTrainerId(UUID.randomUUID().toString());
        trainerDto.setEncryptedPassword(bCryptPasswordEncoder.encode(trainerDto.getPassword()));
        return trainerDto;
    }
}
