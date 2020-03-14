package com.fitcrew.FitCrewAppTrainers.converter;

import org.mapstruct.Mapper;

import com.fitcrew.FitCrewAppModel.domain.model.EmailDto;
import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;

@Mapper
public interface EmailDocumentEmailDtoConverter {
	EmailDto emailDocumentToEmailDto(EmailDocument emailDocument);

	EmailDocument emailDtoToEmailDocument(EmailDto emailDto);
}
