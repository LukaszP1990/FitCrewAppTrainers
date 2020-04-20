package com.fitcrew.FitCrewAppTrainers.service.admin;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;

import java.util.List;

public interface TrainerAdminServiceFacade {
    Either<ErrorMsg, List<TrainerModel>> getTrainers();

    Either<ErrorMsg, TrainerModel> deleteTrainer(String trainerEmail);

    Either<ErrorMsg, TrainerModel> updateTrainer(TrainerDto trainerDto, String trainerEmail);

    Either<ErrorMsg, TrainerModel> getTrainer(String trainerEmail);
}
