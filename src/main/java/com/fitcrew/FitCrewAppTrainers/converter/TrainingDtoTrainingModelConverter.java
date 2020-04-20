package com.fitcrew.FitCrewAppTrainers.converter;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import org.mapstruct.Mapper;

@Mapper
public interface TrainingDtoTrainingModelConverter {
    TrainingModel trainingDtoToTrainingModel(TrainingDto trainingDto);
}
