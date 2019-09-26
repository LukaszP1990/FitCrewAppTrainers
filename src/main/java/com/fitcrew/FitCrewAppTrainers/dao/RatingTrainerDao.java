package com.fitcrew.FitCrewAppTrainers.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerEntity;

@Repository
public interface RatingTrainerDao extends CrudRepository<RatingTrainerEntity, Long> {
}
