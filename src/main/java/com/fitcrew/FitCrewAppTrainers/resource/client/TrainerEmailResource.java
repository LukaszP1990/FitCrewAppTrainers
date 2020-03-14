package com.fitcrew.FitCrewAppTrainers.resource.client;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitcrew.FitCrewAppModel.domain.model.EmailDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.client.TrainerEmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Api(value = "Trainer email resource")
@RestController
@Slf4j
@RequestMapping("/trainer/email/")
class TrainerEmailResource {

	private final TrainerEmailService trainerEmailService;

	public TrainerEmailResource(TrainerEmailService trainerEmailService) {
		this.trainerEmailService = trainerEmailService;
	}

	@ApiOperation(value = "Return sent message to the trainer by client")
	@ApiResponses(value =
			{
					@ApiResponse(code = 200, message = "Successful send a message to the trainer by client response!"),
					@ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
					@ApiResponse(code = 404, message = "404 not found, url is wrong")
			}
	)
	@PostMapping(value = "/sendMessageToTheTrainer",
			consumes = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity sendMessageToTheTrainer(@RequestBody @Valid EmailDto emailDto) {
		log.debug("Email sending to the trainer: {}", emailDto);

		return ResponseResolver.resolve(
				trainerEmailService.sendMessageToTheTrainer(emailDto)
		);
	}
}
