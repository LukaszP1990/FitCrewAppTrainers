package com.fitcrew.FitCrewAppTrainers.service.client.rating;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class TrainerRatingServiceFacadeImpl implements TrainerRatingServiceFacade {

    private final TrainerRatingService trainerRatingService;

    public TrainerRatingServiceFacadeImpl(TrainerRatingService trainerRatingService) {
        this.trainerRatingService = trainerRatingService;
    }

    @Override
    public Either<ErrorMsg, LinkedHashMap<String, Double>> getRankingOfTrainers() {
        return trainerRatingService.getRankingOfTrainers();
    }

    @Override
    public Either<ErrorMsg, Double> getAverageRatingOfTrainer(String trainerEmail) {
        return trainerRatingService.getAverageRatingOfTrainer(trainerEmail);
    }

    @Override
    public Either<ErrorMsg, RatingTrainerModel> setRateForTheTrainer(String trainerEmail, String ratingForTrainer) {
        return trainerRatingService.setRateForTheTrainer(trainerEmail, ratingForTrainer);
    }
}
