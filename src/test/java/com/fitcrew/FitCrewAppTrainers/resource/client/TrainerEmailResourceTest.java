package com.fitcrew.FitCrewAppTrainers.resource.client;

import com.fitcrew.FitCrewAppTrainers.dao.EmailDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.EmailDocument;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.resource.AbstractRestResourceTest;
import com.fitcrew.FitCrewAppTrainers.util.HttpEntityUtil;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrainerEmailResourceTest extends AbstractRestResourceTest {

    private static final List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
    private static final EmailDocument mockedEmailDocument = TrainerResourceMockUtil.createEmailDocument();
    private static final EmailDto mockedEmailDto = TrainerResourceMockUtil.createEmailDto();
    private static String SENDER = "senderTest";
    private static String RECIPIENT = "firstName lastName";
    private static String SUBJECT = "Test message";
    private static String BODY_OF_MESSAGE = "Hi this is a test message";

    @MockBean
    private EmailDao emailDao;

    @MockBean
    private TrainerDao trainerDao;

    @Test
    void sendMessageToTheTrainer() {

        when(trainerDao.findAll()).thenReturn(mockedTrainerDocuments);
        when(emailDao.save(any())).thenReturn(mockedEmailDocument);

        ResponseEntity<EmailDto> responseEntity = restTemplate.postForEntity(
                "/trainer/email/sendMessageToTheTrainer",
                HttpEntityUtil.getHttpEntityWithJwtAndBody(mockedEmailDto),
                EmailDto.class
        );
        EmailDto emailDto = responseEntity.getBody();
        assertNotNull(emailDto);

        assertAll(() -> {
                    assertEquals(SENDER, emailDto.getSender());
                    assertEquals(RECIPIENT, emailDto.getRecipient());
                    assertEquals(SUBJECT, emailDto.getSubject());
                    assertEquals(BODY_OF_MESSAGE, emailDto.getBodyOfMessage());
                }
        );
    }
}