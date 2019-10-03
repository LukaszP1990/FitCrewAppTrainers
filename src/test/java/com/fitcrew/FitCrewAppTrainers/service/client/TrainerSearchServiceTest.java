package com.fitcrew.FitCrewAppTrainers.service.client;

import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerEntity;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerSearchServiceTest {

	private static final List<TrainerEntity> mockedTrainerEntities = TrainerResourceMockUtil.createTrainerEntities();
	private static final TrainerEntity mockedTrainerEntity = TrainerResourceMockUtil.createTrainerEntity();
	private final static String TRAINER_EMAIL = "mockedTrainer@gmail.com";

	@Captor
	private ArgumentCaptor<String> stringArgumentCaptor;

	@Mock
	private TrainerDao trainerDao;

	@InjectMocks
	private TrainerSearchService trainerSearchService;

	@Test
	void shouldGetTrainers() {

		when(trainerDao.findAll()).thenReturn(mockedTrainerEntities);

		Either<ErrorMsg, List<TrainerDto>> listOfTrainers = trainerSearchService.getTrainers();

		verify(trainerDao,times(1)).findAll();

		assertNotNull(listOfTrainers);

		assertAll(() -> {
			assertTrue(listOfTrainers.isRight());
			assertEquals(3, listOfTrainers.get().size());
		});

	}

	@Test
	void shouldNotGetTrainers() {

		Either<ErrorMsg, List<TrainerDto>> emptyListOfTrainers = trainerSearchService.getTrainers();

		assertNotNull(emptyListOfTrainers);
		checkEitherLeft(emptyListOfTrainers.isLeft(),
				"No trainers found",
				emptyListOfTrainers.getLeft());
	}

	@Test
	void shouldGetTrainer() {

		when(trainerDao.findByEmail(anyString()))
				.thenReturn(mockedTrainerEntity);

		Either<ErrorMsg, TrainerDto> trainer =
				trainerSearchService.getTrainer(TRAINER_EMAIL);

		verify(trainerDao, times(1))
				.findByEmail(TRAINER_EMAIL);

		verify(trainerDao)
				.findByEmail(stringArgumentCaptor.capture());

		assertNotNull(trainer);
		assertAll(() -> {
			assertTrue(trainer.isRight());
			assertEquals("firstName", trainer.get().getFirstName());
			assertEquals("lastName", trainer.get().getLastName());
			assertEquals(TRAINER_EMAIL, trainer.get().getEmail());
			assertEquals("501928341", trainer.get().getPhone());
			assertEquals("Description about mock trainer", trainer.get().getSomethingAboutYourself());
		});
	}

	@Test
	void shouldNotGetTrainer() {

		Either<ErrorMsg, TrainerDto> noTrainer =
				trainerSearchService.getTrainer(TRAINER_EMAIL);

		checkEitherLeft(noTrainer.isLeft(),
				"No trainer found",
				noTrainer.getLeft());

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