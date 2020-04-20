package com.fitcrew.FitCrewAppTrainers.service.client.rating;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;

import java.util.LinkedHashMap;

public interface TrainerRatingServiceFacade {
    Either<ErrorMsg, LinkedHashMap<String, Double>> getRankingOfTrainers();

    Either<ErrorMsg, Double> getAverageRatingOfTrainer(String trainerEmail);

    Either<ErrorMsg, RatingTrainerModel> setRateForTheTrainer(String trainerEmail, String ratingForTrainer);
}
