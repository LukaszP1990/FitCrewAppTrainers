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
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerEmailServiceTest {

	private static final List<TrainerEntity> mockedTrainerEntities = TrainerResourceMockUtil.createTrainerEntities();
	private static final EmailEntity mockedEmailEntity = TrainerResourceMockUtil.createEmailEntity();
	private static final EmailDto mockedEmailDto = TrainerResourceMockUtil.createEmailDto();

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
					assertEquals("senderTest", sentEmail.get().getSender());
					assertEquals("firstName lastName1", sentEmail.get().getRecipient());
					assertEquals("Test message", sentEmail.get().getSubject());
					assertEquals("Hi this is a test message", sentEmail.get().getBodyOfMessage());
				}
		);

		verify(trainerDao, times(1)).findAll();
		verifySaveEmailEntity();
	}

	@Test
	void shouldNotSendMessageToTheTrainer(){

		Either<ErrorMsg, EmailDto> noSentEmail =
				trainerEmailService.sendMessageToTheTrainer(mockedEmailDto);

		assertNotNull(noSentEmail);
		checkEitherLeft(true,
				"No email sent because none trainer was found",
				noSentEmail.getLeft());
	}

	private void verifySaveEmailEntity() {
		verify(emailDao, times(1))
				.save(any());

		verify(emailDao)
				.save(emailEntityArgumentCaptor.capture());
	}

	private void checkEitherLeft(boolean value,
								 String message,
								 ErrorMsg errorMsg) {
		assertAll(() -> {
			assertTrue(value);
			assertEquals(message, errorMsg.getMsg());
		});
	}
}