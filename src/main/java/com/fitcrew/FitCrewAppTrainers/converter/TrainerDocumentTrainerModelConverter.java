package com.fitcrew.FitCrewAppTrainers.converter;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import org.mapstruct.Mapper;

@Mapper
public interface TrainerDocumentTrainerModelConverter {
    TrainerModel trainerDocumentToTrainerModel(TrainerDocument trainerDocument);
}
