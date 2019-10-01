package com.fitcrew.FitCrewAppTrainers.service.trainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerSignInServiceTest {

	private final static TrainerEntity mockedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";

	@InjectMocks
	private TrainerSignInService trainerSignInService;

	@Mock
	private TrainerDao trainerDao;


	@Test
	void shouldGetTrainerDetailsByEmail() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);

		TrainerDto trainerDetailsByEmail =
				trainerSignInService.getTrainerDetailsByEmail(TRAINER_EMAIL);

		assertNotNull(trainerDetailsByEmail);
		assertAll(() -> {
			assertEquals("firstName", trainerDetailsByEmail.getFirstName());
			assertEquals("lastName", trainerDetailsByEmail.getLastName());
			assertEquals("01.01.1990", trainerDetailsByEmail.getDateOfBirth());
			assertEquals(
					ENCRYPTED_PASSWORD,
					trainerDetailsByEmail.getEncryptedPassword()
			);
			assertEquals(TRAINER_EMAIL, trainerDetailsByEmail.getEmail());
			assertEquals("501928341", trainerDetailsByEmail.getPhone());
		});
	}

	@Test
	void shouldNotGetTrainerDetailsByEmail() {

		TrainerDto noTrainerDetails = trainerSignInService.getTrainerDetailsByEmail(TRAINER_EMAIL);

		assertNull(noTrainerDetails);
	}
}