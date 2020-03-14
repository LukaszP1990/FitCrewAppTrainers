package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerDto;
import com.fitcrew.FitCrewAppTrainers.converter.RatingTrainerDocumentRatingTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.dao.RatingTrainerDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerRatingService {

	private final TrainerDao trainerDao;
	private final RatingTrainerDao ratingTrainerDao;
	private final RatingTrainerDocumentRatingTrainerDtoConverter ratingTrainerConverter;

	TrainerRatingService(TrainerDao trainerDao,
						 RatingTrainerDao ratingTrainerDao,
						 RatingTrainerDocumentRatingTrainerDtoConverter ratingTrainerConverter) {
		this.trainerDao = trainerDao;
		this.ratingTrainerDao = ratingTrainerDao;
		this.ratingTrainerConverter = ratingTrainerConverter;
	}

	public Either<ErrorMsg, LinkedHashMap<String, Double>> getRankingOfTrainers() {
		return Optional.of(Lists.newArrayList(trainerDao.findAll()))
				.map(this::prepareSortedTrainersByRating)
				.filter(stringDoubleLinkedHashMap -> !stringDoubleLinkedHashMap.isEmpty())
				.map(Either::<ErrorMsg, LinkedHashMap<String, Double>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINERS_SORTED.toString())));
	}

	public Either<ErrorMsg, Double> getAverageRatingOfTrainer(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerDocument -> calculateAverageRating(Long.valueOf(trainerDocument.getId())))
				.map(Either::<ErrorMsg, Double>right)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	public Either<ErrorMsg, RatingTrainerDto> setRateForTheTrainer(String trainerEmail,
																   String ratingForTrainer) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerDocument -> prepareRatingTrainerDocument(ratingForTrainer, trainerDocument))
				.map(ratingTrainerDao::save)
				.map(this::saveRating)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	private Either<ErrorMsg, RatingTrainerDto> saveRating(RatingTrainerDocument savedRating) {
		return Optional.ofNullable(savedRating)
				.map(ratingTrainerConverter::ratingTrainerDocumentToRatingTrainerDto)
				.map(this::checkEitherResponseForRatedTrainer)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.RATING_ERROR.toString())));
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
		Map<String, Double> trainersWithRating = getTrainersWithRatings(trainerDocuments);
		return trainersWithRating.entrySet().stream()
				.sorted(Comparator.comparingDouble(Map.Entry::getValue))
				.collect(
						Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue,
								(a1, a2) -> a1, LinkedHashMap::new)
				);
	}

	private Map<String, Double> getTrainersWithRatings(List<TrainerDocument> trainerDocuments) {
		Map<String, Double> trainersWithRating = new HashMap<>();
		for (TrainerDocument document : trainerDocuments) {
			trainersWithRating.put(
					document.getFirstName() + " " + document.getLastName(),
					calculateAverageRating(Long.valueOf(document.getId())));
		}
		return trainersWithRating;
	}

	private Double calculateAverageRating(Long trainerId) {
		return ratingTrainerDao.findByTrainerId(trainerId)
				.filter(ratingTrainerDocuments -> !ratingTrainerDocuments.isEmpty())
				.map(ratingTrainerDocuments -> ratingTrainerDocuments.stream()
						.mapToDouble(RatingTrainerDocument::getRating)
						.average())
				.filter(OptionalDouble::isPresent)
				.map(OptionalDouble::getAsDouble)
				.get();
	}

	private Either<ErrorMsg, RatingTrainerDto> checkEitherResponseForRatedTrainer(RatingTrainerDto ratingTrainer) {
		return Optional.ofNullable(ratingTrainer)
				.map(Either::<ErrorMsg, RatingTrainerDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}
}
