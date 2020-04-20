package com.fitcrew.FitCrewAppTrainers.service.client;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailDtoConverter;
import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailDtoConverterImpl;
import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailModelConverter;
import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailModelConverterImpl;
import com.fitcrew.FitCrewAppTrainers.dao.EmailDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.service.client.email.TrainerEmailService;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerEmailServiceTest {

    private static final List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
    private static final EmailDocument mockedEmailDocument = TrainerResourceMockUtil.createEmailDocument();
    private static final EmailDto mockedEmailDto = TrainerResourceMockUtil.createEmailDto();
    private static String SENDER = "senderTest";
    private static String RECIPIENT = "firstName lastName";
    private static String SUBJECT = "Test message";
    private static String BODY_OF_MESSAGE = "Hi this is a test message";

    @Captor
    private ArgumentCaptor<EmailDocument> emailDocumentArgumentCaptor;

    private TrainerDao trainerDao = Mockito.mock(TrainerDao.class);
    private EmailDao emailDao = Mockito.mock(EmailDao.class);
    private EmailDocumentEmailModelConverter emailDocumentModelConverter = new EmailDocumentEmailModelConverterImpl();
    private EmailDocumentEmailDtoConverter emailDocumentDtoConverter = new EmailDocumentEmailDtoConverterImpl();
    private TrainerEmailService trainerEmailService = new TrainerEmailService(trainerDao, emailDao, emailDocumentDtoConverter, emailDocumentModelConverter);

    @Test
    void shouldSendMessageToTheTrainer() {
        when(trainerDao.findAll()).thenReturn(mockedTrainerDocuments);
        when(emailDao.save(any(EmailDocument.class))).thenReturn(mockedEmailDocument);

        Either<ErrorMsg, EmailModel> sentEmail =
                trainerEmailService.sendMessageToTheTrainer(mockedEmailDto);

        assertNotNull(sentEmail);
        assertAll(() -> {
                    assertTrue(sentEmail.isRight());
                    assertEquals(SENDER, sentEmail.get().getSender());
                    assertEquals(RECIPIENT, sentEmail.get().getRecipient());
                    assertEquals(SUBJECT, sentEmail.get().getSubject());
                    assertEquals(BODY_OF_MESSAGE, sentEmail.get().getBodyOfMessage());
                }
        );

        verify(trainerDao, times(1)).findAll();
        verifySaveEmailDocument();
    }

    @Test
    void shouldNotSentEmail() {
        when(trainerDao.findAll()).thenReturn(mockedTrainerDocuments);

        Either<ErrorMsg, EmailModel> noSentEmail =
                trainerEmailService.sendMessageToTheTrainer(mockedEmailDto);

        assertNotNull(noSentEmail);
        assertTrue(noSentEmail.isLeft());
        assertEquals(TrainerErrorMessageType.NO_EMAIL_SENT.toString(), noSentEmail.getLeft().getMsg());
    }

    private void verifySaveEmailDocument() {
        verify(emailDao, times(1))
                .save(any());

        verify(emailDao)
                .save(emailDocumentArgumentCaptor.capture());
    }
}