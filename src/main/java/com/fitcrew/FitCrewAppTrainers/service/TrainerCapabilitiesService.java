package com.fitcrew.FitCrewAppTrainers.service;

import com.fitcrew.FitCrewAppTrainers.dto.ClientDto;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignClientService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerCapabilitiesService {

    private final FeignClientService feignClientService;

    public TrainerCapabilitiesService(FeignClientService feignClientService) {
        this.feignClientService = feignClientService;
    }

    public Either<ErrorMsg, List<ClientDto>> getClientsWhoGetTrainingFromTrainer(String trainerName) {
        List<ClientDto> clientsWhoGetTrainingFromTrainer = feignClientService.getClientsWhoGetTrainingFromTrainer(trainerName);
        if (!clientsWhoGetTrainingFromTrainer.isEmpty()) {
            log.debug("List of clients {}", clientsWhoGetTrainingFromTrainer);
            return Either.right(clientsWhoGetTrainingFromTrainer);
        } else {
            log.debug("No client found");
            return Either.left(new ErrorMsg("No client to return"));
        }
    }
}
