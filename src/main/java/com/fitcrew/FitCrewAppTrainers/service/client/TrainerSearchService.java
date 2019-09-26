package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerSearchService {

	private final TrainerDao trainerDao;
	private String SUCCESSFULLY_MAPPING = "Trainer object mapped successfully {}";
	private String NOT_SUCCESSFULLY_MAPPING = "Trainer not mapped successfully";

	public TrainerSearchService(TrainerDao trainerDao) {
		this.trainerDao = trainerDao;
	}

	public Either<ErrorMsg, List<TrainerDto>> getTrainers() {

		ModelMapper modelMapper = prepareModelMapperForExistingTraining();
		ArrayList<TrainerEntity> trainerEntitiesList = prepareTrainerEntitiesList();

		if (!trainerEntitiesList.isEmpty()) {

			log.debug("Trainers found: {}", trainerEntitiesList);
			List<TrainerDto> trainerToReturn = mapTrainerEntityToDto(modelMapper, trainerEntitiesList);

			return checkEitherResponseForTrainers(trainerToReturn,
					SUCCESSFULLY_MAPPING,
					NOT_SUCCESSFULLY_MAPPING);

		} else {
			return Either.left(new ErrorMsg("No trainers found"));
		}
	}

	public Either<ErrorMsg, TrainerDto> getTrainer(String trainerEmail) {

		ModelMapper modelMapper = prepareModelMapperForExistingTraining();
		TrainerEntity trainerEntity = trainerDao.findByEmail(trainerEmail);

		if (trainerEntity != null) {

			log.debug("Trainer found: {}", trainerEntity);
			TrainerDto trainerToReturn = modelMapper.map(trainerEntity, TrainerDto.class);

			return checkEitherResponseForTrainer(trainerToReturn,
					SUCCESSFULLY_MAPPING,
					NOT_SUCCESSFULLY_MAPPING);

		} else {
			return Either.left(new ErrorMsg("No trainer found"));
		}
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

	private Either<ErrorMsg, List<TrainerDto>> checkEitherResponseForTrainers(List<TrainerDto> trainers,
																			  String eitherRightMessage,
																			  String eitherLeftMessage) {
		if (trainers != null) {
			log.debug(eitherRightMessage, trainers);
			return Either.right(trainers);
		} else {
			log.debug(eitherLeftMessage);
			return Either.left(new ErrorMsg(eitherLeftMessage));
		}
	}

	private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer,
																	   String eitherRightMessage,
																	   String eitherLeftMessage) {
		if (trainer != null) {
			log.debug(eitherRightMessage, trainer);
			return Either.right(trainer);
		} else {
			log.debug(eitherLeftMessage);
			return Either.left(new ErrorMsg(eitherLeftMessage));
		}
	}
}
