package com.fitcrew.FitCrewAppTrainers.service.client.email;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;

public interface TrainerEmailServiceFacade {
    Either<ErrorMsg, EmailModel> sendMessageToTheTrainer(EmailDto emailDto);
}
