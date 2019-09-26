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
					"Trainer object mapped successfully and send to trainer web service {}",
					"Trainer not mapped successfully");

		} else {
			return Either.left(new ErrorMsg("No trainers sorted"));
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

	private Either<ErrorMsg, List<TrainerDto>> checkEitherResponseForTrainers(List<TrainerDto> trainer,
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
