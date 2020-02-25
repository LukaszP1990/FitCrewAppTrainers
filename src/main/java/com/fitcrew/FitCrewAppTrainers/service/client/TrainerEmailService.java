package com.fitcrew.FitCrewAppTrainers.service.client;

import java.util.ArrayList;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppModel.domain.model.EmailDto;
import com.fitcrew.FitCrewAppTrainers.dao.EmailDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.EmailEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
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
		String firstName = nameAndSurnameOfRecipient[0];
		String lastName = nameAndSurnameOfRecipient[1];

		return Optional.of(getAllTrainers())
				.map(trainerEntities -> getTrainerEntity(firstName, lastName, trainerEntities))
				.map(trainerEntities -> modelMapper.map(emailDto, EmailEntity.class))
				.map(emailDao::save)
				.map(entity -> modelMapper.map(entity, EmailDto.class))
				.map(Either::<ErrorMsg, EmailDto>right)
				.orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_EMAIL_SENT.toString())));
	}

	private ArrayList<TrainerEntity> getAllTrainers() {
		Iterable<TrainerEntity> trainersEntities = trainerDao.findAll();
		return Lists.newArrayList(trainersEntities);
	}

	private Either<ErrorMsg, TrainerEntity> getTrainerEntity(String firstName,
															 String lastName,
															 ArrayList<TrainerEntity> trainerEntities) {
		return trainerEntities.stream()
				.filter(trainerEntity -> lastName.equals(trainerEntity.getLastName()))
				.filter(trainerEntity -> firstName.equals(trainerEntity.getFirstName()))
				.findFirst()
				.map(Either::<ErrorMsg, TrainerEntity>right)
				.orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}

	private ModelMapper prepareModelMapperForEmail() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
}
