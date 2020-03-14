package com.fitcrew.FitCrewAppTrainers.resource.trainer;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.trainer.TrainerCapabilitiesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Api(value = "Trainer capabilities resource")
@Slf4j
@RestController
@RequestMapping("/trainer")
class TrainerCapabilitiesResource {

    private final TrainerCapabilitiesService trainerCapabilitiesService;

    public TrainerCapabilitiesResource(TrainerCapabilitiesService trainerCapabilitiesService) {
        this.trainerCapabilitiesService = trainerCapabilitiesService;
    }

    @ApiOperation(value = "Return basic informations about trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful basic informations about trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/getBasicInformationAboutTrainer/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getBasicInformationAboutTrainer(@PathVariable String trainerEmail) {
        log.debug("Basic information about trainer by trainer email address: {}", trainerEmail);

        return ResponseResolver.resolve(
                trainerCapabilitiesService.getBasicInformationAboutTrainer(trainerEmail)
        );
    }

    @ApiOperation(value = "Return all trainings from trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful all trainings from trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/getTrainerTrainings/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getTrainerTrainings(@PathVariable String trainerEmail) {
        log.debug("Trainings by trainer email address: {}", trainerEmail);

        return ResponseResolver.resolve(
                trainerCapabilitiesService.getTrainerTrainings(trainerEmail)
        );
    }

    @ApiOperation(value = "Training created by trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training created by trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @PostMapping(value = "/createTraining",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity createTraining(@RequestBody @Valid TrainingDto trainingDto) {
        log.debug("Create training: {}", trainingDto);

        return ResponseResolver.resolve(
                trainerCapabilitiesService.createTraining(trainingDto)
        );
    }

    @ApiOperation(value = "Training deleted by trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training deleted by trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @DeleteMapping(value = "/deleteTraining/{trainerEmail}/trainerEmail/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deleteTraining(@PathVariable String trainerEmail,
                                         @PathVariable String trainingName) {
        log.debug("Delete training by trainer email address: {} \n training name: {}", trainerEmail, trainingName);

        return ResponseResolver.resolve(
                trainerCapabilitiesService.deleteTraining(trainerEmail, trainingName)
        );
    }

    @ApiOperation(value = "Training updated by trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training updated by trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @DeleteMapping(value = "/updateTraining/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateTraining(@RequestBody TrainingDto trainingDto,
                                         @PathVariable String trainerEmail) {
        log.debug("Update training: {} \n by trainer email address: {}", trainingDto, trainerEmail);

        return ResponseResolver.resolve(
                trainerCapabilitiesService.updateTraining(trainingDto, trainerEmail)
        );
    }

    @ApiOperation(value = "Return training to send to the client")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training to send to the client response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/selectTrainingToSend/{trainerEmail}/trainerEmail/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity selectTrainingToSend(@PathVariable String trainerEmail,
                                               @PathVariable String trainingName) {
        log.debug("Send training by trainer email address: {} \n training name: {}", trainerEmail, trainingName);

        return ResponseResolver.resolve(
                trainerCapabilitiesService.selectTrainingToSend(trainerEmail, trainingName)
        );
    }

    @ApiOperation(value = "Return clients who has bought training")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful training to send to the client response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/clientsWhoBoughtTraining/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity clientsWhoBoughtTraining(@PathVariable String trainingName) {
        log.debug("Clients who bought from trainer training: {}", trainingName);

        return ResponseResolver.resolve(
                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(trainingName)
        );
    }
}
