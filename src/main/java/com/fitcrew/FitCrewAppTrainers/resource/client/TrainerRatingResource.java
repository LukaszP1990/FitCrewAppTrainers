package com.fitcrew.FitCrewAppTrainers.resource.client;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.client.TrainerRatingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Api(value = "Trainer rating resource")
@Slf4j
@RestController
@RequestMapping("/trainer")
class TrainerRatingResource {

	private final TrainerRatingService trainerRatingService;

	public TrainerRatingResource(TrainerRatingService trainerRatingService) {
		this.trainerRatingService = trainerRatingService;
	}

	@ApiOperation(value = "Return trainer rating")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful trainer rating response!"),
			@ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
			@ApiResponse(code = 404, message = "404 not found, url is wrong")
	})
	@GetMapping(value = "/getTrainerRating/{trainerEmail}/trainerEmail",
			consumes = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,})
	public ResponseEntity getTrainerRating(@PathVariable String trainerEmail) {

		log.debug("Rating of trainer with email address: {}", trainerEmail);
		Either<ErrorMsg, Double> averageRatingOfTrainer =
				trainerRatingService.getAverageRatingOfTrainer(trainerEmail);

		return ResponseResolver.resolve(averageRatingOfTrainer);
	}

	@ApiOperation(value = "Return trainers ranking sorted by ratings")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful trainers ranking sorted by ratings response!"),
			@ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
			@ApiResponse(code = 404, message = "404 not found, url is wrong")
	})
	@GetMapping(value = "/getTrainersRanking",
			consumes = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,})
	public ResponseEntity getTrainersRanking() {

		return ResponseResolver.resolve(trainerRatingService.getRankingOfTrainers());
	}

	@ApiOperation(value = "Return the rated trainer by the client")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful rated trainer by the client response!"),
			@ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
			@ApiResponse(code = 404, message = "404 not found, url is wrong")
	})
	@PostMapping(value = "/rateTheTrainer/{trainerEmail}/trainerEmail/{ratingForTrainer}/ratingForTrainer",
			consumes = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,})
	public ResponseEntity rateTheTrainer(@PathVariable String trainerEmail,
										 @PathVariable String ratingForTrainer) {

		trainerRatingService.setRateForTheTrainer(trainerEmail, ratingForTrainer);

		return ResponseResolver.resolve(trainerRatingService.getRankingOfTrainers());
	}
}
