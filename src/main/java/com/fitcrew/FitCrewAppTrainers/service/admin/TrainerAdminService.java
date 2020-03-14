package com.fitcrew.FitCrewAppTrainers.service.admin;

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
public class TrainerAdminService {

	private final TrainerDao trainerDao;
	private final TrainerDocumentTrainerDtoConverter trainerConverter;

	TrainerAdminService(TrainerDao trainerDao,
						TrainerDocumentTrainerDtoConverter trainerConverter) {
		this.trainerDao = trainerDao;
		this.trainerConverter = trainerConverter;
	}

	public Either<ErrorMsg, List<TrainerDto>> getTrainers() {
		return Optional.ofNullable(trainerDao.findAll())
				.map(Lists::newArrayList)
				.filter(trainerDocuments -> !trainerDocuments.isEmpty())
				.map(this::mapTrainerDocumentsToDtoObjects)
				.map(this::checkEitherResponseForTrainers)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	public Either<ErrorMsg, TrainerDto> deleteTrainer(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(this::prepareSuccessfulClientDeleting)
				.map(this::checkEitherResponseForTrainer)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER_DELETED.toString())));
	}

	public Either<ErrorMsg, TrainerDto> updateTrainer(TrainerDto trainerDto,
													  String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerDocument -> prepareTrainingUpdate(trainerDto, trainerDocument))
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER_UPDATED.toString())));
	}

	public Either<ErrorMsg, TrainerDto> getTrainer(String trainerEmail) {
		return trainerDao.findByEmail(trainerEmail)
				.map(trainerConverter::trainerDocumentToTrainerDto)
				.map(this::checkEitherResponseForTrainer)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	private TrainerDto prepareSuccessfulClientDeleting(TrainerDocument trainerDocument) {
		trainerDao.delete(trainerDocument);
		return trainerConverter.trainerDocumentToTrainerDto(trainerDocument);
	}

	private Either<ErrorMsg, TrainerDto> prepareTrainingUpdate(TrainerDto trainerDto,
															   TrainerDocument trainerDocument) {
		setNewValuesForClient(trainerDto, trainerDocument);
		return checkEitherResponseForTrainer(
				trainerConverter.trainerDocumentToTrainerDto(trainerDocument)
		);
	}

	private void setNewValuesForClient(TrainerDto trainerDto,
									   TrainerDocument foundTrainerDocumentByEmail) {
		foundTrainerDocumentByEmail.setTrainerId(trainerDto.getTrainerId());
		foundTrainerDocumentByEmail.setDateOfBirth(trainerDto.getDateOfBirth());
		foundTrainerDocumentByEmail.setEncryptedPassword(trainerDto.getEncryptedPassword());
		foundTrainerDocumentByEmail.setFirstName(trainerDto.getFirstName());
		foundTrainerDocumentByEmail.setLastName(trainerDto.getLastName());
		foundTrainerDocumentByEmail.setPhone(trainerDto.getPhone());
	}

	private Either<ErrorMsg, List<TrainerDto>> checkEitherResponseForTrainers(List<TrainerDto> trainersToReturn) {
		return Optional.ofNullable(trainersToReturn)
				.filter(trainers -> !trainers.isEmpty())
				.map(Either::<ErrorMsg, List<TrainerDto>>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_CLIENT_FOUND.toString())));
	}

	private Either<ErrorMsg, TrainerDto> checkEitherResponseForTrainer(TrainerDto trainer) {
		return Optional.ofNullable(trainer)
				.map(Either::<ErrorMsg, TrainerDto>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NOT_SUCCESSFULLY_MAPPING.toString())));
	}

	private List<TrainerDto> mapTrainerDocumentsToDtoObjects(ArrayList<TrainerDocument> trainerDocuments) {
		return trainerDocuments.stream()
				.map(trainerConverter::trainerDocumentToTrainerDto)
				.collect(Collectors.toList());
	}
}
