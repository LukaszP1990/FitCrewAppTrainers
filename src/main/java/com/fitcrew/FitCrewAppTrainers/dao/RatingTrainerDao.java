package com.fitcrew.FitCrewAppTrainers.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;

@Repository
public interface RatingTrainerDao extends MongoRepository<RatingTrainerDocument, String> {
	Optional<List<RatingTrainerDocument>> findByTrainerId(Long id);
}
