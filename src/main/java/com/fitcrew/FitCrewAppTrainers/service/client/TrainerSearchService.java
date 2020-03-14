package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerSearchService {

	private final TrainerDao trainerDao;
	private final TrainerDocumentTrainerDtoConverter trainerConverter;

	TrainerSearchService(TrainerDao trainerDao,
						 TrainerDocumentTrainerDtoConverter trainerConverter) {
		this.trainerDao = trainerDao;
		this.trainerConverter = trainerConverter;
	}

	public Either<ErrorMsg, List<TrainerDto>> getTrainers() {
		return Optional.of(Lists.newArrayList(trainerDao.findAll()))
				.filter(trainerDocuments -> !trainerDocuments.isEmpty())
				.map(this::mapTrainerDocumentToDto)
				.map(this::checkEitherResponseForTrainers)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINERS.toString())));
	}

	public Either<ErrorMsg, TrainerDto> getTrainer(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerConverter::trainerDocumentToTrainerDto)
				.map(this::checkEitherResponseForTrainer)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	private List<TrainerDto> mapTrainerDocumentToDto(ArrayList<TrainerDocument> trainerDocuments) {
		return trainerDocuments.stream()
				.map(trainerConverter::trainerDocumentToTrainerDto)
				.collect(Collectors.toList());
	}

	private Either<ErrorMsg, List<TrainerDto>> checkEitherResponseForTrainers(List<TrainerDto> trainers) {
		return Optional.ofNullable(trainers)
				.filter(trainersList -> !trainersList.isEmpty())
				.map(Either::<ErrorMsg, List<TrainerDto>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer) {
		return Optional.ofNullable(trainer)
				.map(Either::<ErrorMsg, TrainerDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}
}
