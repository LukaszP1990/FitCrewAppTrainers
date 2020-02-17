package com.fitcrew.FitCrewAppTrainers.service.trainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerSignInServiceTest {

	private final static TrainerEntity mockedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
	private static String TRAINER_FIRST_NAME = "firstName";
	private static String TRAINER_LAST_NAME = "lastName";
	private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
	private static String TRAINER_PHONE_NUMBER = "501928341";

	@Captor
	private ArgumentCaptor<String> stringArgumentCaptor;

	@InjectMocks
	private TrainerSignInService trainerSignInService;

	@Mock
	private TrainerDao trainerDao;

	@Test
	void shouldGetTrainerDetailsByEmail() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerEntity));

		Either<ErrorMsg, TrainerDto> trainerDetailsByEmail =
				trainerSignInService.getTrainerDetailsByEmail(TRAINER_EMAIL);

		verify(trainerDao, times(1))
				.findByEmail(TRAINER_EMAIL);
		verify(trainerDao)
				.findByEmail(stringArgumentCaptor.capture());

		assertNotNull(trainerDetailsByEmail);
		assertAll(() -> {
			assertEquals(TRAINER_FIRST_NAME, trainerDetailsByEmail.get().getFirstName());
			assertEquals(TRAINER_LAST_NAME, trainerDetailsByEmail.get().getLastName());
			assertEquals(TRAINER_DATE_OF_BIRTH, trainerDetailsByEmail.get().getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					trainerDetailsByEmail.get().getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, trainerDetailsByEmail.get().getEmail());
			assertEquals(TRAINER_PHONE_NUMBER, trainerDetailsByEmail.get().getPhone());
		});
	}

	@Test
	void shouldNotGetTrainerDetailsByEmail() {

		Either<ErrorMsg, TrainerDto> noTrainerDetails = trainerSignInService.getTrainerDetailsByEmail(TRAINER_EMAIL);
		assertNotNull(noTrainerDetails);
		assertTrue(noTrainerDetails.isLeft());
		assertEquals(TrainerErrorMessageType.NO_TRAINER.toString(), noTrainerDetails.getLeft().getMsg());
	}

	@Test
	void shouldLoadUserByUsername() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(Optional.of(mockedTrainerEntity));

		UserDetails trainerDetailsByEmail = trainerSignInService.loadUserByUsername(TRAINER_EMAIL);

		verify(trainerDao, times(1))
				.findByEmail(TRAINER_EMAIL);
		verify(trainerDao)
				.findByEmail(stringArgumentCaptor.capture());

		assertNotNull(trainerDetailsByEmail);
		assertAll(() -> {
			assertEquals(TRAINER_EMAIL, trainerDetailsByEmail.getUsername());
			assertEquals(ENCRYPTED_PASSWORD, trainerDetailsByEmail.getPassword());
		});
	}

	@Test
	void shouldNotLoadUserByUsername() {

		UserDetails userByUsername = trainerSignInService.loadUserByUsername(TRAINER_EMAIL);
		assertNull(userByUsername);
	}
}