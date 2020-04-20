package com.fitcrew.FitCrewAppTrainers.converter;

import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import org.mapstruct.Mapper;

@Mapper
public interface EmailDocumentEmailDtoConverter {
    EmailDocument emailDtoToEmailDocument(EmailDto emailDto);
}
