package com.fitcrew.FitCrewAppTrainers.service.trainer;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerSignInService implements UserDetailsService {

	private final TrainerDao trainerDao;
	private final TrainerDocumentTrainerDtoConverter trainerConverter;

	TrainerSignInService(TrainerDao trainerDao,
						 TrainerDocumentTrainerDtoConverter trainerConverter) {
		this.trainerDao = trainerDao;
		this.trainerConverter = trainerConverter;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Trainer searched in database by username: {}", username);
		return trainerDao.findByEmail(username)
				.map(trainerDocument -> new User(trainerDocument.getEmail(),
						trainerDocument.getEncryptedPassword(),
						true,
						true,
						true,
						true,
						new ArrayList<>()))
				.orElse(null);
	}

	public Either<ErrorMsg, TrainerDto> getTrainerDetailsByEmail(String email) {
		return trainerDao.findByEmail(email)
				.map(trainerConverter::trainerDocumentToTrainerDto)
				.map(Either::<ErrorMsg, TrainerDto>right)
				.orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}
}
