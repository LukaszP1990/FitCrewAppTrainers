package com.fitcrew.FitCrewAppTrainers.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerEntity;

@Repository
public interface RatingTrainerDao extends MongoRepository<RatingTrainerEntity, Long> {
	Optional<List<RatingTrainerEntity>> findByTrainerId(Long id);
}
