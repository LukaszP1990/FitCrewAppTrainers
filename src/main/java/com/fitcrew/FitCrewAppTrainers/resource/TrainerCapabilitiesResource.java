package com.fitcrew.FitCrewAppTrainers.resource;

import com.fitcrew.FitCrewAppTrainers.dto.ClientDto;
import com.fitcrew.FitCrewAppTrainers.feignclient.FeignClientService;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.TrainerCapabilitiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Trainer capabilities resource")
@Slf4j
@RestController
@RequestMapping("/trainer")
public class TrainerCapabilitiesResource {

    private final TrainerCapabilitiesService trainerCapabilitiesService;

    public TrainerCapabilitiesResource(TrainerCapabilitiesService trainerCapabilitiesService) {
        this.trainerCapabilitiesService = trainerCapabilitiesService;
    }

    @ApiOperation(value = "Return all clients who bought training from trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful all clients who bought training from trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/getClientsWhoGetTraining/{trainerName}/trainerName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,})
    public ResponseEntity getClientWhoGetTraining(@PathVariable String trainerName) {
        Either<ErrorMsg, List<ClientDto>> clientsWhoGetTrainingFromTrainer =
                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(trainerName);

        return ResponseResolver.resolve(clientsWhoGetTrainingFromTrainer);
    }
}
