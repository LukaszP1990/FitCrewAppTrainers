package com.fitcrew.FitCrewAppTrainers.service.client.email;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class TrainerEmailServiceFacadeImpl implements TrainerEmailServiceFacade {

    private final TrainerEmailService trainerEmailService;

    public TrainerEmailServiceFacadeImpl(TrainerEmailService trainerEmailService) {
        this.trainerEmailService = trainerEmailService;
    }

    @Override
    public Either<ErrorMsg, EmailModel> sendMessageToTheTrainer(EmailDto emailDto) {
        return trainerEmailService.sendMessageToTheTrainer(emailDto);
    }
}
