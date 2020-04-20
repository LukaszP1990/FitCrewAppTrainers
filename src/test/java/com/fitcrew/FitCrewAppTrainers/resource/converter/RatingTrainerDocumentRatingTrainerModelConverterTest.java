package com.fitcrew.FitCrewAppTrainers.resource.converter;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.RatingTrainerDocumentRatingTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.domains.RatingTrainerDocument;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class RatingTrainerDocumentRatingTrainerModelConverterTest {

    private static RatingTrainerDocument ratingTrainerDocument = TrainerResourceMockUtil.createRatingTrainerDocument();
    private RatingTrainerDocumentRatingTrainerModelConverter ratingTrainerConverter =
            Mappers.getMapper(RatingTrainerDocumentRatingTrainerModelConverter.class);
    private static String FIRST_NAME = "mockedName";
    private static String LAST_NAME = "mockedName";
    private static int rating = 2;

    @Test
    void shouldConvertRatingTrainerDocumentToRatingTrainerModel() {
        RatingTrainerModel ratingTrainerModel = ratingTrainerConverter.ratingTrainerDocumentToRatingTrainerModel(ratingTrainerDocument);

        assertNotNull(ratingTrainerModel);
        assertAll(() -> {
            assertEquals(FIRST_NAME, ratingTrainerModel.getFirstName());
            assertEquals(LAST_NAME, ratingTrainerModel.getLastName());
            assertEquals(rating, ratingTrainerModel.getRating());
        });
    }
}
