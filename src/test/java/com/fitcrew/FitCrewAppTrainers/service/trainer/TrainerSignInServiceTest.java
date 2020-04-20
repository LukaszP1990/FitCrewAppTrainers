package com.fitcrew.FitCrewAppTrainers.service.trainer;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverterImpl;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.service.trainer.signin.TrainerSignInService;
import com.fitcrew.FitCrewAppTrainers.util.TrainerResourceMockUtil;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerSignInServiceTest {

    private final static TrainerDocument mockedEmailDocument = TrainerResourceMockUtil.createTrainerDocument();
    private static String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    private static String ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";
    private static String TRAINER_FIRST_NAME = "firstName";
    private static String TRAINER_LAST_NAME = "lastName";
    private static String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    private static String TRAINER_PHONE_NUMBER = "501928341";

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private TrainerDao trainerDao = Mockito.mock(TrainerDao.class);
    private TrainerDocumentTrainerModelConverter trainerConverter = new TrainerDocumentTrainerModelConverterImpl();
    @InjectMocks
    private TrainerSignInService trainerSignInService = new TrainerSignInService(trainerDao, trainerConverter);

    @Test
    void shouldGetTrainerDetailsByEmail() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedEmailDocument));

        Either<ErrorMsg, TrainerModel> trainerDetailsByEmail =
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

        Either<ErrorMsg, TrainerModel> noTrainerDetails = trainerSignInService.getTrainerDetailsByEmail(TRAINER_EMAIL);
        assertNotNull(noTrainerDetails);
        assertTrue(noTrainerDetails.isLeft());
        assertEquals(TrainerErrorMessageType.NO_TRAINER.toString(), noTrainerDetails.getLeft().getMsg());
    }

    @Test
    void shouldLoadUserByUsername() {

        when(trainerDao.findByEmail(anyString()))
                .thenReturn(Optional.of(mockedEmailDocument));

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