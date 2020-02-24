package com.fitcrew.FitCrewAppTrainers.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.FitCrewAppTrainers.domains.EmailEntity;

@Repository
public interface EmailDao extends MongoRepository<EmailEntity, Long> {
}
