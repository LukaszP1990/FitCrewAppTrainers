package com.fitcrew.FitCrewAppTrainers.resource.converter;

import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailDtoConverter;
import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class EmailDocumentEmailDtoConverterTest {

    private static EmailDto emailDto = TrainerResourceMockUtil.createEmailDto();
    private EmailDocumentEmailDtoConverter emailConverter = Mappers.getMapper(EmailDocumentEmailDtoConverter.class);
    private static String SENDER = "senderTest";
    private static String RECIPIENT = "firstName lastName";
    private static String SUBJECT = "Test message";
    private static String BODY_OF_MESSAGE = "Hi this is a test message";

    @Test
    void shouldConvertEmailDtoToEmailDocument() {
        EmailDocument emailDocument = emailConverter.emailDtoToEmailDocument(emailDto);

        assertNotNull(emailDocument);
        assertAll(() -> {
            assertEquals(SENDER, emailDocument.getSender());
            assertEquals(RECIPIENT, emailDocument.getRecipient());
            assertEquals(SUBJECT, emailDocument.getSubject());
            assertEquals(BODY_OF_MESSAGE, emailDocument.getBodyOfMessage());
        });
    }
}