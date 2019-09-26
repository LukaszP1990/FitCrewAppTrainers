package com.fitcrew.FitCrewAppTrainers.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerCreateService {

	private final TrainerDao trainerDao;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private static int trainerId = 1;

	public TrainerCreateService(TrainerDao trainerDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.trainerDao = trainerDao;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public Either<ErrorMsg, TrainerDto> createTrainer(TrainerDto trainerDto) {

		setPredefinedData(trainerDto);

		ModelMapper modelMapper = prepareModelMapper();

		TrainerEntity trainerEntity = modelMapper.map(trainerDto, TrainerEntity.class);
		TrainerEntity savedTrainer = trainerDao.save(trainerEntity);

		return checkIfTrainerWasSaved(savedTrainer, modelMapper);

	}

	private void setPredefinedData(TrainerDto trainerDto) {
		trainerDto.setTrainerId(UUID.randomUUID().toString());
		trainerDto.setEncryptedPassword(bCryptPasswordEncoder.encode(trainerDto.getPassword()));
	}

	private PropertyMap<TrainerDto, TrainerEntity> skipModifiedFieldsMap = new PropertyMap<TrainerDto, TrainerEntity>() {
		protected void configure() {
			skip().setId(trainerId);
			trainerId++;
		}
	};

	private ModelMapper prepareModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(skipModifiedFieldsMap);
		return modelMapper;
	}

	private Either<ErrorMsg, TrainerDto> checkIfTrainerWasSaved(TrainerEntity savedTrainer, ModelMapper modelMapper) {
		if (savedTrainer != null) {

			log.debug("Trainer saved successfully: {}", savedTrainer);
			TrainerDto returnTrainer = modelMapper.map(savedTrainer, TrainerDto.class);

			return Either.right(returnTrainer);
		} else {
			log.debug("Trainer save failed");
			return Either.left(new ErrorMsg("Trainer save failed"));
		}
	}
}
