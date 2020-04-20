package com.fitcrew.FitCrewAppTrainers.service.trainer.create;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class TrainerCreateServiceImpl implements TrainerCreateServiceFacade {

    private final TrainerCreateService trainerCreateService;

    public TrainerCreateServiceImpl(TrainerCreateService trainerCreateService) {
        this.trainerCreateService = trainerCreateService;
    }

    @Override
    public Either<ErrorMsg, TrainerModel> createTrainer(TrainerDto trainerDto) {
        return trainerCreateService.createTrainer(trainerDto);
    }
}
