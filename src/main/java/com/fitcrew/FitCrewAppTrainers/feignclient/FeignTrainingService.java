package com.fitcrew.FitCrewAppTrainers.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;

@FeignClient(name = "training-ws", path = "/training")
public interface FeignTrainingService {

    @GetMapping("/getTrainerTrainings/{trainerEmail}/trainerEmail")
    List<TrainingDto> getTrainerTrainings(@PathVariable String trainerEmail);

    @PostMapping("/createTraining")
    TrainingDto createTraining(@RequestBody TrainingDto trainingDto);

    @DeleteMapping("/deleteTraining/{trainerEmail}/trainerEmail/{trainingName}/trainingName")
    TrainingDto deleteTraining(@PathVariable String trainerEmail,
                               @PathVariable String trainingName);

    @PutMapping("/updateTraining/{trainerEmail}/trainerEmail")
    TrainingDto updateTraining(@RequestBody TrainingDto trainingDto,
                               @PathVariable String trainingName);

    @GetMapping("/selectTraining/{trainerEmail}/trainerEmail/{trainingName}/trainingName")
    TrainingDto selectTraining(@PathVariable String trainerEmail,
                               @PathVariable String trainingName);

    @GetMapping(value = "/clientsWhoBoughtTraining/{trainingName}/trainingName")
    List<String> clientsWhoBoughtTraining(@PathVariable String trainingName);
}
