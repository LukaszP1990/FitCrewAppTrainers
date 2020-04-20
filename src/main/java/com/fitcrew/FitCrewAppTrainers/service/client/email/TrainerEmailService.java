package com.fitcrew.FitCrewAppTrainers.service.client.email;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailDtoConverter;
import com.fitcrew.FitCrewAppTrainers.converter.EmailDocumentEmailModelConverter;
import com.fitcrew.FitCrewAppTrainers.dao.EmailDao;
import com.fitcrew.FitCrewAppTrainers.dao.TrainerDao;
import com.fitcrew.FitCrewAppTrainers.domains.TrainerDocument;
import com.fitcrew.FitCrewAppTrainers.dto.EmailDto;
import com.fitcrew.FitCrewAppTrainers.enums.TrainerErrorMessageType;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.google.common.collect.Lists;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class TrainerEmailService {

    private final TrainerDao trainerDao;
    private final EmailDao emailDao;
    private final EmailDocumentEmailDtoConverter emailDocumentDtoConverter;
    private final EmailDocumentEmailModelConverter emailDocumentModelConverter;

    public TrainerEmailService(TrainerDao trainerDao,
                               EmailDao emailDao,
                               EmailDocumentEmailDtoConverter emailDocumentDtoConverter,
                               EmailDocumentEmailModelConverter emailDocumentModelConverter) {
        this.trainerDao = trainerDao;
        this.emailDao = emailDao;
        this.emailDocumentDtoConverter = emailDocumentDtoConverter;
        this.emailDocumentModelConverter = emailDocumentModelConverter;
    }

    public Either<ErrorMsg, EmailModel> sendMessageToTheTrainer(EmailDto emailDto) {
        String[] nameAndSurnameOfRecipient = emailDto.getRecipient().split(" ");
        String firstName = nameAndSurnameOfRecipient[0];
        String lastName = nameAndSurnameOfRecipient[1];

        return Optional.of(Lists.newArrayList(trainerDao.findAll()))
                .map(trainerDocuments -> getTrainerDocument(firstName, lastName, trainerDocuments))
                .map(trainerDocument -> emailDocumentDtoConverter.emailDtoToEmailDocument(emailDto))
                .map(emailDao::save)
                .map(emailDocumentModelConverter::emailDocumentToEmailModel)
                .map(Either::<ErrorMsg, EmailModel>right)
                .orElseGet(() -> Either.left(new ErrorMsg(TrainerErrorMessageType.NO_EMAIL_SENT.toString())));
    }

    private Either<ErrorMsg, TrainerDocument> getTrainerDocument(String firstName,
                                                                 String lastName,
                                                                 ArrayList<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .filter(trainerDocument -> lastName.equals(trainerDocument.getLastName()))
                .filter(trainerDocument -> firstName.equals(trainerDocument.getFirstName()))
                .findFirst()
                .map(Either::<ErrorMsg, TrainerDocument>right)
                .orElse(Either.left(new ErrorMsg(TrainerErrorMessageType.NO_TRAINER.toString())));
    }
}
