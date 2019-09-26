package com.fitcrew.FitCrewAppTrainers.resource.client;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.client.TrainerSearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

@Api(value = "Search trainers resource")
@Slf4j
@RestController
@RequestMapping("/trainer")
class TrainerSearchResource {

	private final TrainerSearchService trainerSearchService;

	public TrainerSearchResource(TrainerSearchService trainerSearchService) {
		this.trainerSearchService = trainerSearchService;
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
					MediaType.APPLICATION_XML_VALUE,},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,})
	public ResponseEntity getTrainers() {

		return ResponseResolver.resolve(trainerSearchService.getTrainers());
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
					MediaType.APPLICATION_XML_VALUE,},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,})
	public ResponseEntity getTrainer(@PathVariable String trainerEmail) {

		Either<ErrorMsg, TrainerDto> trainerFound =
				trainerSearchService.getTrainer(trainerEmail);

		return ResponseResolver.resolve(trainerFound);
	}
}
