package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.RatingTrainerDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.RatingTrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerRatingService {

	private final TrainerDao trainerDao;
	private final RatingTrainerDao ratingTrainerDao;

	public TrainerRatingService(TrainerDao trainerDao,
								RatingTrainerDao ratingTrainerDao) {
		this.trainerDao = trainerDao;
		this.ratingTrainerDao = ratingTrainerDao;
	}

	public Either<ErrorMsg, LinkedHashMap<String, Double>> getRankingOfTrainers() {

		Iterable<TrainerEntity> trainersEntities = trainerDao.findAll();
		ArrayList<TrainerEntity> trainerEntitiesList = Lists.newArrayList(trainersEntities);

		LinkedHashMap<String, Double> sortedTrainersByRating =
				prepareSortedTrainersByRating(trainerEntitiesList);

		if (!sortedTrainersByRating.isEmpty()) {

			List<String> sortedTrainersNames = new ArrayList<>(sortedTrainersByRating.keySet());
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


	public Either<ErrorMsg, RatingTrainerDto> setRateForTheTrainer(String trainerEmail,
																   String ratingForTrainer) {

		ModelMapper modelMapper = prepareModelMapperForExistingTraining();
		TrainerEntity trainerEntity = trainerDao.findByEmail(trainerEmail);

		if (trainerEntity != null) {

			RatingTrainerEntity ratingTrainerEntity = prepareRatingTrainerEntity(ratingForTrainer, trainerEntity);
			log.debug("Rating to save {}", ratingTrainerEntity);

			RatingTrainerEntity savedRating =
					ratingTrainerDao.save(ratingTrainerEntity);

			return tryToSaveRating(modelMapper, savedRating);
		} else {
			log.debug("Trainer not found");
			return Either.left(new ErrorMsg("Trainer not found"));
		}

	}

	private Either<ErrorMsg, RatingTrainerDto> tryToSaveRating(ModelMapper modelMapper,
															   RatingTrainerEntity savedRating) {
		if (savedRating != null) {
			RatingTrainerDto ratedTrainerToReturn = modelMapper.map(savedRating, RatingTrainerDto.class);

			return checkEitherResponseForRatedTrainer(ratedTrainerToReturn,
					"Rated trainer object mapped successfully {}",
					"Rated trainer object not mapped successfully");
		} else {
			log.debug("No rating to save");
			return Either.left(new ErrorMsg("No rating to save"));
		}
	}

	private RatingTrainerEntity prepareRatingTrainerEntity(String ratingForTrainer,
														   TrainerEntity trainerEntity) {
		return RatingTrainerEntity.builder()
				.firstName(trainerEntity.getFirstName())
				.lastName(trainerEntity.getFirstName())
				.rating(Integer.parseInt(ratingForTrainer))
				.trainerEntity(trainerEntity)
				.build();
	}

	private LinkedHashMap<String, Double> prepareSortedTrainersByRating(ArrayList<TrainerEntity> trainerEntitiesList) {
		Map<String, Double> trainerNameAndRatingMap = trainerEntitiesList.stream()
				.collect(Collectors.toMap(
						trainerEntity -> trainerEntity.getFirstName() + " " + trainerEntity.getLastName(),
						this::calculateAverageRating,
						(oldValue, newValue) -> oldValue
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

	private ModelMapper prepareModelMapperForExistingTraining() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	private Either<ErrorMsg, RatingTrainerDto> checkEitherResponseForRatedTrainer(RatingTrainerDto ratingTrainer,
																				  String eitherRightMessage,
																				  String eitherLeftMessage) {
		if (ratingTrainer != null) {
			log.debug(eitherRightMessage, ratingTrainer);
			return Either.right(ratingTrainer);
		} else {
			log.debug(eitherLeftMessage);
			return Either.left(new ErrorMsg(eitherLeftMessage));
		}
	}
}
