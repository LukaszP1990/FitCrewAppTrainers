package com.fitcrew.FitCrewAppTrainers.resource.converter;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailModelConverter;
import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class EmailDocumentEmailModelConverterTest {

    private static EmailDocument emailDocument = TrainerResourceMockUtil.createEmailDocument();
    private EmailDocumentEmailModelConverter emailConverter = Mappers.getMapper(EmailDocumentEmailModelConverter.class);
    private static String SENDER = "senderTest";
    private static String RECIPIENT = "firstName lastName";
    private static String SUBJECT = "Test message";
    private static String BODY_OF_MESSAGE = "Hi this is a test message";

    @Test
    void shouldConvertEmailDocumentToEmailModel() {
        EmailModel emailModel = emailConverter.emailDocumentToEmailModel(emailDocument);

        assertNotNull(emailModel);
        assertAll(() -> {
            assertEquals(SENDER, emailModel.getSender());
            assertEquals(RECIPIENT, emailModel.getRecipient());
            assertEquals(SUBJECT, emailModel.getSubject());
            assertEquals(BODY_OF_MESSAGE, emailModel.getBodyOfMessage());
        });
    }
}
