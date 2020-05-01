package com.fitcrew.FitCrewAppTrainers.service.client.rating;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.RatingTrainerDocumentRatingTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.dao.RatingTrainerDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainerRatingService {

    private final TrainerDao trainerDao;
    private final RatingTrainerDao ratingTrainerDao;
    private final RatingTrainerDocumentRatingTrainerModelConverter ratingTrainerConverter;

    public TrainerRatingService(TrainerDao trainerDao,
                                RatingTrainerDao ratingTrainerDao,
                                RatingTrainerDocumentRatingTrainerModelConverter ratingTrainerConverter) {
        this.trainerDao = trainerDao;
        this.ratingTrainerDao = ratingTrainerDao;
        this.ratingTrainerConverter = ratingTrainerConverter;
    }

    public Either<ErrorMsg, LinkedHashMap<String, Double>> getRankingOfTrainers() {
        return Optional.ofNullable(trainerDao.findAll())
                .map(this::prepareSortedTrainersByRating)
                .filter(stringDoubleLinkedHashMap -> !stringDoubleLinkedHashMap.isEmpty())
                .map(Either::<ErrorMsg, LinkedHashMap<String, Double>>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINERS_SORTED.toString())));
    }

    public Either<ErrorMsg, Double> getAverageRatingOfTrainer(String trainerEmail) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerDocument -> calculateAverageRating(Long.valueOf(trainerDocument.getId())))
                .map(Either::<ErrorMsg, Double>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    public Either<ErrorMsg, RatingTrainerModel> setRateForTheTrainer(String trainerEmail,
                                                                     String ratingForTrainer) {
        return trainerDao.findByEmail(trainerEmail)
                .map(trainerDocument -> prepareRatingTrainerDocument(ratingForTrainer, trainerDocument))
                .map(ratingTrainerDao::save)
                .map(this::saveRating)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }

    private Either<ErrorMsg, RatingTrainerModel> saveRating(RatingTrainerDocument savedRating) {
        return Optional.ofNullable(savedRating)
                .map(ratingTrainerConverter::ratingTrainerDocumentToRatingTrainerModel)
                .map(this::checkEitherResponseForRatedTrainer)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.RATING_ERROR.toString())));
    }

    private RatingTrainerDocument prepareRatingTrainerDocument(String ratingForTrainer,
                                                               TrainerDocument trainerDocument) {
        return RatingTrainerDocument.builder()
                .firstName(trainerDocument.getFirstName())
                .lastName(trainerDocument.getFirstName())
                .rating(Integer.parseInt(ratingForTrainer))
                .trainerId(Long.valueOf(trainerDocument.getId()))
                .build();
    }

    private LinkedHashMap<String, Double> prepareSortedTrainersByRating(List<TrainerDocument> trainerDocuments) {
        var trainersWithRating = getTrainersWithRatings(trainerDocuments);
        return trainersWithRating.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .collect(
                        Collectors.toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a1, a2) -> a1, LinkedHashMap::new)
                );
    }

    private Map<String, Double> getTrainersWithRatings(List<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .collect(Collectors.toMap(
                        trainerDocument -> trainerDocument.getFirstName() + " " + trainerDocument.getLastName(),
                        trainerDocument -> calculateAverageRating(Long.valueOf(trainerDocument.getId())))
                );
    }

    private Double calculateAverageRating(Long trainerId) {
        return ratingTrainerDao.findByTrainerId(trainerId)
                .map(ratingTrainerDocuments -> ratingTrainerDocuments.stream()
                        .mapToDouble(RatingTrainerDocument::getRating)
                        .average())
                .filter(OptionalDouble::isPresent)
                .map(OptionalDouble::getAsDouble)
                .get();
    }

    private Either<ErrorMsg, RatingTrainerModel> checkEitherResponseForRatedTrainer(RatingTrainerModel ratingTrainer) {
        return Optional.ofNullable(ratingTrainer)
                .map(Either::<ErrorMsg, RatingTrainerModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
    }
}
