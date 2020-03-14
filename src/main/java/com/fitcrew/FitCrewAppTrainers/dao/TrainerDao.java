package com.fitcrew.FitCrewAppTrainers.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;

@Repository
public interface TrainerDao extends MongoRepository<TrainerDocument, String> {
	Optional<TrainerDocument> findByEmail(String email);
}
