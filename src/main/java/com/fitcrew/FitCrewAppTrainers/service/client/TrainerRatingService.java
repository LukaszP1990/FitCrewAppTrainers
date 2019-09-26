package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerRatingService {

	private final TrainerDao trainerDao;

	public TrainerRatingService(TrainerDao trainerDao) {
		this.trainerDao = trainerDao;
	}

	public Either<ErrorMsg, LinkedHashMap<String, Double>> getRankingOfTrainers() {

		Iterable<TrainerEntity> trainersEntities = trainerDao.findAll();
		ArrayList<TrainerEntity> trainerEntitiesList = Lists.newArrayList(trainersEntities);

		LinkedHashMap<String, Double> sortedTrainersByRating =
				prepareSortedTrainersByRating(trainerEntitiesList);

		if (!sortedTrainersByRating.isEmpty()) {

			List<String> sortedTrainersNames = new ArrayList<>(sortedTrainersByRating
					.keySet());
			log.debug("Sorted trainers by rating: {}", sortedTrainersNames);
			return Either.right(sortedTrainersByRating);
		} else {
			log.debug("No trainers sorted");
			return Either.left(new ErrorMsg("No trainers sorted"));
		}
	}

	public Either<ErrorMsg, Double> getAverageRatingOfTrainer(String trainerEmail) {

		TrainerEntity trainerEntity = trainerDao.findByEmail(trainerEmail);

		if (trainerEntity != null) {
			log.debug("Average rating of trainer {}", trainerEntity);
			double averageRating = calculateAverageRating(trainerEntity);

			log.debug("Calculation result {}", averageRating);
			return Either.right(averageRating);
		} else {
			log.debug("Trainer not found to calculate the average grade");
			return Either.left(new ErrorMsg("Trainer not found to calculate the average grade"));
		}

	}

	private LinkedHashMap<String, Double> prepareSortedTrainersByRating(ArrayList<TrainerEntity> trainerEntitiesList) {
		Map<String, Double> trainerNameAndRatingMap = trainerEntitiesList.stream()
				.collect(Collectors.toMap(
						trainerEntity -> trainerEntity.getLastName() + trainerEntity.getFirstName(),
						this::calculateAverageRating
				));

		return trainerNameAndRatingMap.entrySet().stream()
				.sorted(Comparator.comparingDouble(Map.Entry::getValue))
				.collect(
						Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue,
								(a1, a2) -> a1, LinkedHashMap::new)
				);
	}

	private double calculateAverageRating(TrainerEntity trainerEntity) {
		return trainerEntity.getRatingTrainerEntity().stream()
				.mapToDouble(RatingTrainerEntity::getRating)
				.average()
				.orElse(Double.NaN);
	}
}
