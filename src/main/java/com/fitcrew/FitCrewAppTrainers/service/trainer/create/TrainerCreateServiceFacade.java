package com.fitcrew.FitCrewAppTrainers.service.trainer.create;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;

public interface TrainerCreateServiceFacade {
    Either<ErrorMsg, TrainerModel> createTrainer(TrainerDto trainerDto);
}
