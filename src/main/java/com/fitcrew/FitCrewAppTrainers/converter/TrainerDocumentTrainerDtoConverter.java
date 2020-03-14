package com.fitcrew.FitCrewAppTrainers.converter;

import org.mapstruct.Mapper;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;

@Mapper
public interface TrainerDocumentTrainerDtoConverter {
	TrainerDto trainerDocumentToTrainerDto(TrainerDocument trainerDocument);

	TrainerDocument trainerDtoToClientDocument(TrainerDto trainerDto);
}
