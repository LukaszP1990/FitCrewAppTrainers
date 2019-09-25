package com.fitcrew.FitCrewAppTrainers.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "training-ws")
public interface FeignTrainingService {

//    @GetMapping("/getAllTrainers")
//    List<TrainerDto> getListsOfTrainers();
//
//    @GetMapping("/getTrainerDetails/firstName/{firstName}/lastName/{lastName}")
//    TrainerDto getTrainerDetails(@PathVariable String firstName,
//                                 @PathVariable String lastName);
//
//    @PostMapping("/rateTheTrainer")
//    RatingTrainerDto rateTheTrainer(@RequestBody RatingTrainerDto ratingTrainerDto);
//
//    @PostMapping("/sendMessageToTheTrainer")
//    EmailDto sendMessageToTheTrainer(@RequestBody EmailDto emailDto);
}
