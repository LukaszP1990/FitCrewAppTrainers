package com.fitcrew.FitCrewAppTrainers.feignclient;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "training-service", path = "/training")
public interface FeignTrainingService {

    @GetMapping(value = "/getTrainerTrainings/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    List<TrainingModel> getTrainerTrainings(@PathVariable String trainerEmail);

    @PostMapping(value = "/createTraining",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    TrainingModel createTraining(@RequestBody TrainingModel trainingModel);

    @DeleteMapping(value = "/deleteTraining/{trainerEmail}/trainerEmail/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    TrainingModel deleteTraining(@PathVariable String trainerEmail,
                                 @PathVariable String trainingName);

    @PutMapping(value = "/updateTraining/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    TrainingModel updateTraining(@RequestBody TrainingModel trainingModel,
                                 @PathVariable String trainerEmail);

    @GetMapping(value = "/selectTraining/{trainerEmail}/trainerEmail/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    TrainingModel selectTraining(@PathVariable String trainerEmail,
                                 @PathVariable String trainingName);

    @GetMapping(value = "/clientsWhoBoughtTraining/{trainingName}/trainingName",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    List<String> clientsWhoBoughtTraining(@PathVariable String trainingName);
}
