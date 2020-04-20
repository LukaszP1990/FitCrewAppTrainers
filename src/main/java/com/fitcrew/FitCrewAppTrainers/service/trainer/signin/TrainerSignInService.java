package com.fitcrew.FitCrewAppTrainers.service.trainer.signin;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.converter.TrainerDocumentTrainerModelConverter;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class TrainerSignInService implements UserDetailsService {

    private final TrainerDao trainerDao;
    private final TrainerDocumentTrainerModelConverter trainerConverter;

    public TrainerSignInService(TrainerDao trainerDao,
                                TrainerDocumentTrainerModelConverter trainerConverter) {
        this.trainerDao = trainerDao;
        this.trainerConverter = trainerConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Trainer searched in database by username: {}", username);
        return trainerDao.findByEmail(username)
                .map(trainerDocument -> new User(trainerDocument.getEmail(),
                        trainerDocument.getEncryptedPassword(),
                        true,
                        true,
                        true,
                        true,
                        new ArrayList<>()))
                .orElse(null);
    }

    public Either<ErrorMsg, TrainerModel> getTrainerDetailsByEmail(String email) {
        return trainerDao.findByEmail(email)
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .map(Either::<ErrorMsg, TrainerModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }
}
