package com.fitcrew.FitCrewAppTrainers.resource.trainer;

import java.util.List;

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

import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.trainer.TrainerCapabilitiesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
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
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getClientWhoGetTraining(@PathVariable String trainerName) {
//        Either<ErrorMsg, List<ClientDto>> clientsWhoGetTrainingFromTrainer =
//                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(trainerName);
//
//        return ResponseResolver.resolve(clientsWhoGetTrainingFromTrainer);
        return null;
    }

    @ApiOperation(value = "Return basic informations about trainer")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful basic informations about trainer response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @GetMapping(value = "/getBasicInformationsAboutTrainer/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getBasicInformationsAboutTrainer(@PathVariable String trainerEmail) {

        Either<ErrorMsg, TrainerDto> basicInformationsAboutTrainer =
                trainerCapabilitiesService.getBasicInformationsAboutTrainer(trainerEmail);

        return ResponseResolver.resolve(basicInformationsAboutTrainer);
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

        Either<ErrorMsg, List<TrainingDto>> trainerTrainings =
                trainerCapabilitiesService.getTrainerTrainings(trainerEmail);

        return ResponseResolver.resolve(trainerTrainings);
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
    public ResponseEntity createTrainingByTrainer(@RequestBody @Valid TrainingDto trainingDto) {

        Either<ErrorMsg, TrainingDto> trainerTrainings =
                trainerCapabilitiesService.createTraining(trainingDto);

        return ResponseResolver.resolve(trainerTrainings);
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

        Either<ErrorMsg, TrainingDto> trainerTrainings =
                trainerCapabilitiesService.deleteTraining(trainerEmail, trainingName);

        return ResponseResolver.resolve(trainerTrainings);
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

        Either<ErrorMsg, TrainingDto> trainerTrainings =
                trainerCapabilitiesService.updateTraining(trainingDto, trainerEmail);

        return ResponseResolver.resolve(trainerTrainings);
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

        Either<ErrorMsg, TrainingDto> basicInformationsAboutTrainer =
                trainerCapabilitiesService.selectTrainingToSend(trainerEmail, trainingName);

        return ResponseResolver.resolve(basicInformationsAboutTrainer);
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
    public ResponseEntity clientsWhoBoughtTraining(@PathVariable String trainerEmail) {

        Either<ErrorMsg, List<String>> clients =
                trainerCapabilitiesService.getClientsWhoGetTrainingFromTrainer(trainerEmail);

        return ResponseResolver.resolve(clients);
    }
}
