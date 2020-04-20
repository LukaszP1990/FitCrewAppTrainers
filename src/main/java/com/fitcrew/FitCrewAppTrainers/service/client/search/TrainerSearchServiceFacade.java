package com.fitcrew.FitCrewAppTrainers.service.client.search;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;

import java.util.List;

public interface TrainerSearchServiceFacade {
    Either<ErrorMsg, List<TrainerModel>> getTrainers();

    Either<ErrorMsg, TrainerModel> getTrainer(String trainerEmail);
}
