package com.fitcrew.FitCrewAppTrainers.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;

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
		TrainerEntity trainerEntity = trainerDao.findByEmail(username);

		if (trainerEntity == null) {
			log.debug("Trainer not found in database");
			return null;
		}
		return new User(trainerEntity.getEmail(),
				trainerEntity.getEncryptedPassword(),
				true,
				true,
				true,
				true,
				new ArrayList<>());
	}

	public TrainerDto getTrainerDetailsByEmail(String email) {

		log.debug("Trainer searched in database by email: {}", email);
		TrainerEntity trainerEntity = trainerDao.findByEmail(email);

		if (trainerEntity == null) {
			log.debug("Trainer not found in database");
			return null;
		}

		return new ModelMapper().map(trainerEntity, TrainerDto.class);
	}
}
