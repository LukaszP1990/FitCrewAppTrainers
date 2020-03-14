package com.fitcrew.FitCrewAppTrainers.service.trainer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverter;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerDtoConverterImpl;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;

import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerCreateServiceTest {

	private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
	private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
	private static final TrainerDto mockedTrainerDto = TrainerResourceMockUtil.createTrainerDto();
	private static String TRAINER_FIRST_NAME = "firstName";
	private static String TRAINER_LAST_NAME = "lastName";
	private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
	private static String TRAINER_PHONE_NUMBER = "501928341";
	private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";

	@Captor
	private ArgumentCaptor<TrainerDocument> trainerDocumentArgumentCaptor;

	private TrainerDao trainerDao = Mockito.mock(TrainerDao.class);
	private BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
	private TrainerDocumentTrainerDtoConverter trainerConverter = new TrainerDocumentTrainerDtoConverterImpl();

	private TrainerCreateService trainerCreateService = new TrainerCreateService(trainerDao, bCryptPasswordEncoder, trainerConverter);

	@Test
	void shouldCreateTrainer() {

		when(trainerDao.save(any(TrainerDocument.class)))
				.thenReturn(mockedTrainerDocument);
		when(bCryptPasswordEncoder.encode(anyString()))
				.thenReturn(ENCRYPTED_PASSWORD);

		Either<ErrorMsg, TrainerDto> trainerDto = trainerCreateService.createTrainer(mockedTrainerDto);
		assertNotNull(trainerDto);

		verify(trainerDao, times(1))
				.save(any());

		verify(trainerDao)
				.save(trainerDocumentArgumentCaptor.capture());

		assertAll(() -> {
			assertTrue(trainerDto.isRight());
			assertEquals(TRAINER_FIRST_NAME, trainerDto.get().getFirstName());
			assertEquals(TRAINER_LAST_NAME, trainerDto.get().getLastName());
			assertEquals(TRAINER_DATE_OF_BIRTH, trainerDto.get().getDateOfBirth());
			assertEquals(ENCRYPTED_PASSWORD, trainerDto.get().getEncryptedPassword());
			assertEquals(TRAINER_EMAIL, trainerDto.get().getEmail());
			assertEquals(TRAINER_PHONE_NUMBER, trainerDto.get().getPhone());
		});
	}

	@Test
	void shouldNotCreateClientDocument() {

		Either<ErrorMsg, TrainerDto> noTrainer =
				trainerCreateService.createTrainer(mockedTrainerDto);

		assertNotNull(noTrainer);
		assertTrue(noTrainer.isLeft());
		assertEquals(TrainerErrorMessageType.NO_TRAINING_CREATED.toString(), noTrainer.getLeft().getMsg());
	}
}