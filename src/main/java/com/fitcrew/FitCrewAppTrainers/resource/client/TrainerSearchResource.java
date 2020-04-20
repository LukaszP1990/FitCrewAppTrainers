package com.fitcrew.FitCrewAppTrainers.resource.client;

import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.client.search.TrainerSearchServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Search trainers resource")
@Slf4j
@RestController
@RequestMapping("/trainer/search/")
class TrainerSearchResource {

    private final TrainerSearchServiceFacade trainerSearchServiceFacade;

    public TrainerSearchResource(TrainerSearchServiceFacade trainerSearchServiceFacade) {
        this.trainerSearchServiceFacade = trainerSearchServiceFacade;
    }

    @ApiOperation(value = "Return all trainers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful trainers search response!"),
            @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
            @ApiResponse(code = 404, message = "404 not found, url is wrong")
    })
    @GetMapping(value = "/getTrainers",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getTrainers() {

        return ResponseResolver.resolve(trainerSearchServiceFacade.getTrainers());
    }

    @ApiOperation(value = "Return single trainer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful single trainer search response!"),
            @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
            @ApiResponse(code = 404, message = "404 not found, url is wrong")
    })
    @GetMapping(value = "/getTrainer/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getTrainer(@PathVariable String trainerEmail) {
        log.debug("Basic information about trainer by trainer email address: {}", trainerEmail);

        return ResponseResolver.resolve(
                trainerSearchServiceFacade.getTrainer(trainerEmail)
        );
    }
}
