package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerSearchService {

	private final TrainerDao trainerDao;

	public TrainerSearchService(TrainerDao trainerDao) {
		this.trainerDao = trainerDao;
	}

	public Either<ErrorMsg, List<TrainerDto>> getTrainers() {

		ModelMapper modelMapper = prepareModelMapperForExistingTraining();
		ArrayList<TrainerEntity> trainerEntitiesList = prepareTrainerEntitiesList();

		return Optional.of(trainerEntitiesList)
				.filter(trainerEntities -> !trainerEntities.isEmpty())
				.map(trainerEntities -> mapTrainerEntityToDto(modelMapper, trainerEntities))
				.map(this::checkEitherResponseForTrainers)
				.orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINERS.toString())));
	}

	public Either<ErrorMsg, TrainerDto> getTrainer(String trainerEmail) {

		ModelMapper modelMapper = prepareModelMapperForExistingTraining();

		return trainerDao.findByEmail(trainerEmail)
				.map(trainerEntity -> modelMapper.map(trainerEntity, TrainerDto.class))
				.map(this::checkEitherResponseForTrainer)
				.orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	private ArrayList<TrainerEntity> prepareTrainerEntitiesList() {
		Iterable<TrainerEntity> trainersEntities = trainerDao.findAll();
		return Lists.newArrayList(trainersEntities);
	}

	private List<TrainerDto> mapTrainerEntityToDto(ModelMapper modelMapper,
												   ArrayList<TrainerEntity> trainerEntitiesList) {
		return trainerEntitiesList.stream()
				.map(trainerEntity -> modelMapper.map(trainerEntity, TrainerDto.class))
				.collect(Collectors.toList());
	}

	private ModelMapper prepareModelMapperForExistingTraining() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	private Either<ErrorMsg, List<TrainerDto>> checkEitherResponseForTrainers(List<TrainerDto> trainers) {
		return Optional.ofNullable(trainers)
				.filter(trainersList -> !trainersList.isEmpty())
				.map(Either::<ErrorMsg,List<TrainerDto>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer) {
		return Optional.ofNullable(trainer)
				.map(Either::<ErrorMsg, TrainerDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}
}
