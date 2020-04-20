package com.fitcrew.FitCrewAppTrainers.converter;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import org.mapstruct.Mapper;

@Mapper
public interface EmailDocumentEmailModelConverter {
    EmailModel emailDocumentToEmailModel(EmailDocument emailDocument);
}
