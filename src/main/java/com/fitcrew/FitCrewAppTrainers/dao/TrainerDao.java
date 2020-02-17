package com.fitcrew.FitCrewAppTrainers.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;

@Repository
public interface TrainerDao extends CrudRepository<TrainerEntity, Long> {
	Optional<TrainerEntity> findByEmail(String email);
}
