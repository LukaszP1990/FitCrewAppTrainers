package com.fitcrew.FitCrewAppTrainers.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.FitCrewAppTrainers.domains.EmailEntity;

@Repository
public interface EmailDao extends CrudRepository<EmailEntity, Long> {

}
