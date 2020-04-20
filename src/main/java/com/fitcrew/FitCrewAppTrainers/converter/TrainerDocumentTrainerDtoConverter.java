package com.fitcrew.FitCrewAppTrainers.converter;

import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import org.mapstruct.Mapper;

@Mapper
public interface TrainerDocumentTrainerDtoConverter {
    TrainerDocument trainerDtoToTrainerDocument(TrainerDto trainerDto);
}
