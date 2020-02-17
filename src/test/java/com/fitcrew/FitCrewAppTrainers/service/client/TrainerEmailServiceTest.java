package com.fitcrew.FitCrewAppTrainers.service.client;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppTrainers.dao.EmailDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.EmailEntity;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerEmailServiceTest {

	private static final List<TrainerEntity> mockedTrainerEntities = TrainerResourceMockUtil.createTrainerEntities();
	private static final EmailEntity mockedEmailEntity = TrainerResourceMockUtil.createEmailEntity();
	private static final EmailDto mockedEmailDto = TrainerResourceMockUtil.createEmailDto();
	private static String SENDER = "senderTest";
	private static String RECIPIENT = "firstName lastName";
	private static String SUBJECT = "Test message";
	private static String BODY_OF_MESSAGE = "Hi this is a test message";

	@Captor
	private ArgumentCaptor<EmailEntity> emailEntityArgumentCaptor;

	@Mock
	private TrainerDao trainerDao;

	@Mock
	private EmailDao emailDao;

	@InjectMocks
	TrainerEmailService trainerEmailService;

	@Test
	void shouldSendMessageToTheTrainer() {
		when(trainerDao.findAll()).thenReturn(mockedTrainerEntities);
		when(emailDao.save(any(EmailEntity.class))).thenReturn(mockedEmailEntity);

		Either<ErrorMsg, EmailDto> sentEmail =
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
		verifySaveEmailEntity();
	}

	@Test
	void shouldNotSentEmail(){
		when(trainerDao.findAll()).thenReturn(mockedTrainerEntities);

		Either<ErrorMsg, EmailDto> noSentEmail =
				trainerEmailService.sendMessageToTheTrainer(mockedEmailDto);

		assertNotNull(noSentEmail);
		assertTrue(noSentEmail.isLeft());
		assertEquals(TrainerErrorMessageType.NO_EMAIL_SENT.toString(), noSentEmail.getLeft().getMsg());
	}

	private void verifySaveEmailEntity() {
		verify(emailDao, times(1))
				.save(any());

		verify(emailDao)
				.save(emailEntityArgumentCaptor.capture());
	}
}