package com.fitcrew.FitCrewAppTrainers.converter;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import org.mapstruct.Mapper;

@Mapper
public interface RatingTrainerDocumentRatingTrainerModelConverter {
    RatingTrainerModel ratingTrainerDocumentToRatingTrainerModel(RatingTrainerDocument ratingTrainerDocument);
}
