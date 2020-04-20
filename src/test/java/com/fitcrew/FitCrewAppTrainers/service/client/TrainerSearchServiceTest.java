package com.fitcrew.FitCrewAppTrainers.service.client;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverterImpl;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.service.client.search.TrainerSearchService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerSearchServiceTest {

    private static final List<TrainerDocument> mockedTrainerDocuments = TrainerResourceMockUtil.createTrainerDocuments();
    private static final TrainerDocument mockedTrainerDocument = TrainerResourceMockUtil.createTrainerDocument();
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_DESCRIPTION = "Description about mock trainer";
    private static String TRAINER_PHONE_NUMBER = "501928341";

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private TrainerDao trainerDao = Mockito.mock(TrainerDao.class);
    private TrainerDocumentTrainerModelConverter trainerConverter = new TrainerDocumentTrainerModelConverterImpl();

    private TrainerSearchService trainerSearchService = new TrainerSearchService(trainerDao, trainerConverter);

    @Test
    void shouldGetTrainers() {

        when(trainerDao.findAll()).thenReturn(mockedTrainerDocuments);

        Either<ErrorMsg, List<TrainerModel>> listOfTrainers = trainerSearchService.getTrainers();

        verify(trainerDao, times(1)).findAll();
        assertNotNull(listOfTrainers);
        assertAll(() -> {
            assertTrue(listOfTrainers.isRight());
            assertEquals(3, listOfTrainers.get().size());
        });

    }

    @Test
    void shouldNotGetTrainers() {

        Either<ErrorMsg, List<TrainerModel>> emptyListOfTrainers = trainerSearchService.getTrainers();

        assertNotNull(emptyListOfTrainers);
        checkEitherLeft(emptyListOfTrainers.isLeft(), TrainerErrorMessageType.NO_TRAINERS, emptyListOfTrainers.getLeft());
    }

    @Test
    void shouldGetTrainer() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedTrainerDocument));

        Either<ErrorMsg, TrainerModel> trainer =
                trainerSearchService.getTrainer(TRAINER_EMAIL);

        verify(trainerDao, times(1))
                .findByEmail(TRAINER_EMAIL);
        verify(trainerDao)
                .findByEmail(stringArgumentCaptor.capture());

        assertNotNull(trainer);
        assertAll(() -> {
            assertTrue(trainer.isRight());
            assertEquals(TRAINER_FIRST_NAME, trainer.get().getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainer.get().getLastName());
            assertEquals(TRAINER_EMAIL, trainer.get().getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, trainer.get().getPhone());
            assertEquals(TRAINER_DESCRIPTION, trainer.get().getSomethingAboutYourself());
        });
    }

    @Test
    void shouldNotGetTrainer() {

        Either<ErrorMsg, TrainerModel> noTrainer =
                trainerSearchService.getTrainer(TRAINER_EMAIL);

        assertNotNull(noTrainer);
        checkEitherLeft(noTrainer.isLeft(), TrainerErrorMessageType.NO_TRAINER, noTrainer.getLeft());

    }

    private void checkEitherLeft(boolean ifLeft,
                                 TrainerErrorMessageType errorMessageType,
                                 ErrorMsg errorMsgEitherLeft) {
        assertTrue(ifLeft);
        assertEquals(errorMessageType.toString(), errorMsgEitherLeft.getMsg());
    }
}