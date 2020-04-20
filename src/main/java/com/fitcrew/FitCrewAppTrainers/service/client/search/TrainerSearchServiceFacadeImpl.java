package com.fitcrew.FitCrewAppTrainers.service.client.search;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerSearchServiceFacadeImpl implements TrainerSearchServiceFacade {

    private final TrainerSearchService trainerSearchService;

    public TrainerSearchServiceFacadeImpl(TrainerSearchService trainerSearchService) {
        this.trainerSearchService = trainerSearchService;
    }

    @Override
    public Either<ErrorMsg, List<TrainerModel>> getTrainers() {
        return trainerSearchService.getTrainers();
    }

    @Override
    public Either<ErrorMsg, TrainerModel> getTrainer(String trainerEmail) {
        return trainerSearchService.getTrainer(trainerEmail);
    }
}
