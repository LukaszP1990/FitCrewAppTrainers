package com.fitcrew.FitCrewAppTrainers.resource.trainer;

import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.trainer.create.TrainerCreateServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Trainer sign up resource")
@Slf4j
@RestController
@RequestMapping("/trainer")
public class TrainerCreateResource {

    private final TrainerCreateServiceFacade trainerCreateServiceFacade;

    public TrainerCreateResource(TrainerCreateServiceFacade trainerCreateServiceFacade) {
        this.trainerCreateServiceFacade = trainerCreateServiceFacade;
    }

    @ApiOperation(value = "Create new trainer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful trainer create response!"),
            @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
            @ApiResponse(code = 404, message = "404 not found, url is wrong")
    })
    @PostMapping(value = "/createTrainer",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,})
    public ResponseEntity createTrainer(@RequestBody TrainerDto trainerDto) {

        log.debug("Trainer to save: {}", trainerDto);

        return ResponseResolver.resolve(
                trainerCreateServiceFacade.createTrainer(trainerDto)
        );
    }
}
