package com.fitcrew.FitCrewAppTrainers.dao;

import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerDao extends CrudRepository<TrainerEntity, Long> {
	TrainerEntity findByEmail(String email);
}
