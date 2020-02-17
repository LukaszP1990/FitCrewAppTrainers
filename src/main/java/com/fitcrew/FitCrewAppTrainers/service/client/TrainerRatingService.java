package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.RatingTrainerDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.RatingTrainerDto;
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

	public TrainerRatingService(TrainerDao trainerDao,
								RatingTrainerDao ratingTrainerDao) {
		this.trainerDao = trainerDao;
		this.ratingTrainerDao = ratingTrainerDao;
	}

	public Either<ErrorMsg, LinkedHashMap<String, Double>> getRankingOfTrainers() {

		Iterable<TrainerEntity> trainersEntities = trainerDao.findAll();

		return Optional.of(Lists.newArrayList(trainersEntities))
				.map(this::prepareSortedTrainersByRating)
				.filter(stringDoubleLinkedHashMap -> !stringDoubleLinkedHashMap.isEmpty())
				.map(Either::<ErrorMsg, LinkedHashMap<String, Double>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINERS_SORDER.toString())));
	}

	public Either<ErrorMsg, Double> getAverageRatingOfTrainer(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(this::calculateAverageRating)
				.map(Either::<ErrorMsg, Double>right)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	public Either<ErrorMsg, RatingTrainerDto> setRateForTheTrainer(String trainerEmail,
																   String ratingForTrainer) {
		ModelMapper modelMapper = prepareModelMapperForExistingTraining();

		return trainerDao.findByEmail(trainerEmail)
				.map(trainerEntity ->  prepareRatingTrainerEntity(ratingForTrainer, trainerEntity))
				.map(ratingTrainerDao::save)
				.map(ratingTrainerEntity -> tryToSaveRating(modelMapper,ratingTrainerEntity))
				.orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	private Either<ErrorMsg, RatingTrainerDto> tryToSaveRating(ModelMapper modelMapper,
															   RatingTrainerEntity savedRating) {
		return Optional.ofNullable(savedRating)
				.map(ratingTrainerEntity -> modelMapper.map(ratingTrainerEntity, RatingTrainerDto.class))
				.map(this::checkEitherResponseForRatedTrainer)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.RATING_ERROR.toString())));
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

	private Either<ErrorMsg, RatingTrainerDto> checkEitherResponseForRatedTrainer(RatingTrainerDto ratingTrainer) {
		return Optional.ofNullable(ratingTrainer)
				.map(Either::<ErrorMsg, RatingTrainerDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private ModelMapper prepareModelMapperForExistingTraining() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
}
