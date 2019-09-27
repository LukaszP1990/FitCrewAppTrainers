package com.fitcrew.FitCrewAppTrainers.feignclient;

import com.fitcrew.FitCrewAppTrainers.dto.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "client-ws", path = "/client")
public interface FeignClientService {

    @GetMapping(value = "/getClientsWhoGetTraining/{trainerName}/trainerName")
    List<ClientDto> getClientsWhoGetTrainingFromTrainer(@PathVariable String trainerName);
}
