package com.fitcrew.FitCrewAppTrainers.service.trainer;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerCreateService {

	private final TrainerDao trainerDao;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private static int trainerId = 1;

	public TrainerCreateService(TrainerDao trainerDao,
								BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.trainerDao = trainerDao;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public Either<ErrorMsg, TrainerDto> createTrainer(TrainerDto trainerDto) {

		ModelMapper modelMapper = prepareModelMapper();
		setPredefinedData(trainerDto);

		return Optional.of(trainerDto)
				.map(trainer -> modelMapper.map(trainer, TrainerEntity.class))
				.map(trainerDao::save)
				.map(trainerEntity -> modelMapper.map(trainerEntity,TrainerDto.class))
				.map(Either::<ErrorMsg, TrainerDto>right)
				.orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINING_CREATED.toString())));
	}

	private void setPredefinedData(TrainerDto trainerDto) {
		trainerDto.setTrainerId(UUID.randomUUID().toString());
		trainerDto.setEncryptedPassword(bCryptPasswordEncoder.encode(trainerDto.getPassword()));
	}

	private ModelMapper prepareModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(skipModifiedFieldsMap);
		return modelMapper;
	}

	private PropertyMap<TrainerDto, TrainerEntity> skipModifiedFieldsMap = new PropertyMap<TrainerDto, TrainerEntity>() {
		protected void configure() {
			skip().setTrainerEntityId(trainerId);
			trainerId++;
		}
	};
}
