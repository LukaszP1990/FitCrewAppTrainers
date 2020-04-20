package com.fitcrew.FitCrewAppTrainers.service.admin;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerAdminServiceFacadeImpl implements TrainerAdminServiceFacade {

    private final TrainerAdminService trainerAdminService;

    public TrainerAdminServiceFacadeImpl(TrainerAdminService trainerAdminService) {
        this.trainerAdminService = trainerAdminService;
    }

    @Override
    public Either<ErrorMsg, List<TrainerModel>> getTrainers() {
        return trainerAdminService.getTrainers();
    }

    @Override
    public Either<ErrorMsg, TrainerModel> deleteTrainer(String trainerEmail) {
        return trainerAdminService.deleteTrainer(trainerEmail);
    }

    @Override
    public Either<ErrorMsg, TrainerModel> updateTrainer(TrainerDto trainerDto, String trainerEmail) {
        return trainerAdminService.updateTrainer(trainerDto, trainerEmail);
    }

    @Override
    public Either<ErrorMsg, TrainerModel> getTrainer(String trainerEmail) {
        return trainerAdminService.getTrainer(trainerEmail);
    }
}
