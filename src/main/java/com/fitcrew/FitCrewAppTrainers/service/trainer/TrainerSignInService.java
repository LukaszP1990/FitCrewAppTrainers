package com.fitcrew.FitCrewAppTrainers.service.trainer;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerSignInService implements UserDetailsService {

	private final TrainerDao trainerDao;

	public TrainerSignInService(TrainerDao trainerDao) {
		this.trainerDao = trainerDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Trainer searched in database by username: {}", username);
		return trainerDao.findByEmail(username)
				.map(trainerEntity -> new User(trainerEntity.getEmail(),
						trainerEntity.getEncryptedPassword(),
						true,
						true,
						true,
						true,
						new ArrayList<>()))
				.orElse(null);

	}

	public Either<ErrorMsg, TrainerDto> getTrainerDetailsByEmail(String email) {
		return trainerDao.findByEmail(email)
				.map(trainerEntity -> new ModelMapper().map(trainerEntity, TrainerDto.class))
				.map(Either::<ErrorMsg, TrainerDto>right)
				.orElseGet(()->Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
	}
}
