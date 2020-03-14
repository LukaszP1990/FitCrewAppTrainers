package com.fitcrew.FitCrewAppTrainers.converter;

import org.mapstruct.Mapper;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerDto;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;

@Mapper
public interface RatingTrainerDocumentRatingTrainerDtoConverter {
	RatingTrainerDto ratingTrainerDocumentToRatingTrainerDto(RatingTrainerDocument ratingTrainerDocument);
}
