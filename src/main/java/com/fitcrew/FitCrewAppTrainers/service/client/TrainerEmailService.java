package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.EmailDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.EmailEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerEmailService {

	private final TrainerDao trainerDao;
	private final EmailDao emailDao;

	public TrainerEmailService(TrainerDao trainerDao,
							   EmailDao emailDao) {
		this.trainerDao = trainerDao;
		this.emailDao = emailDao;
	}

	public Either<ErrorMsg, EmailDto> sendMessageToTheTrainer(EmailDto emailDto) {

		ModelMapper modelMapper = prepareModelMapperForEmail();

		String[] nameAndSurnameOfRecipient = emailDto.getRecipient().split(" ");
		String firstNameToCheck = nameAndSurnameOfRecipient[0];
		String lastNameToCheck = nameAndSurnameOfRecipient[1];

		ArrayList<TrainerEntity> trainerEntitiesList = getAllTrainers();

		TrainerEntity foundTrainer = getTrainerByFirstAndLastName(
				firstNameToCheck,
				lastNameToCheck,
				trainerEntitiesList);

		if (foundTrainer != null) {

			EmailEntity emailEntityToSave = modelMapper.map(emailDto, EmailEntity.class);
			log.debug("Email to save for trainer: {}",
					foundTrainer.getFirstName() + " " + foundTrainer.getLastName());

			EmailEntity savedEmail = emailDao.save(emailEntityToSave);

			return checkIfEmailWasSaved(savedEmail, modelMapper);

		} else {
			log.error("None trainer was found: {}",
					firstNameToCheck + " " + lastNameToCheck);

			return Either.left(new ErrorMsg("No email sent because none trainer was found"));
		}
	}

	private Either<ErrorMsg, EmailDto> checkIfEmailWasSaved(EmailEntity savedEmail,
															ModelMapper modelMapper) {
		if (savedEmail != null) {

			log.debug("Email saved successfully: {}", savedEmail);
			EmailDto returnEmail = modelMapper.map(savedEmail, EmailDto.class);

			return Either.right(returnEmail);
		} else {
			log.debug("Email save failed");
			return Either.left(new ErrorMsg("Email save failed"));
		}
	}

	private ArrayList<TrainerEntity> getAllTrainers() {
		Iterable<TrainerEntity> trainersEntities = trainerDao.findAll();
		return Lists.newArrayList(trainersEntities);
	}

	private TrainerEntity getTrainerByFirstAndLastName(String firstNameToCheck,
													   String lastNameToCheck,
													   ArrayList<TrainerEntity> trainerEntitiesList) {
		return trainerEntitiesList.stream()
				.filter(trainerEntity -> lastNameToCheck.equals(trainerEntity.getLastName()))
				.filter(trainerEntity -> firstNameToCheck.equals(trainerEntity.getFirstName()))
				.findFirst()
				.orElse(null);
	}

	private ModelMapper prepareModelMapperForEmail() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

}
