package com.fitcrew.FitCrewAppTrainers.resource.admin;

import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.admin.TrainerAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Resource for admin web service")
@Slf4j
@RestController
@RequestMapping("/trainer")
class TrainerAdminResource {

    private final TrainerAdminService trainerAdminService;

    public TrainerAdminResource(TrainerAdminService trainerAdminService) {
        this.trainerAdminService = trainerAdminService;
    }

    @ApiOperation(value = "Return all trainers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful get all trainers response!"),
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
        return ResponseResolver.resolve(trainerAdminService.getTrainers());
    }

    @ApiOperation(value = "Return trainer who has been deleted")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful trainer who has been deleted response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @DeleteMapping(value = "/deleteTrainer/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,})
    private ResponseEntity deleteTrainer(@PathVariable String trainerEmail) {

        return ResponseResolver.resolve(trainerAdminService.deleteTrainer(trainerEmail));
    }

    @ApiOperation(value = "Return trainer who has been updated")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "Successful trainer who has been updated response!"),
                    @ApiResponse(code = 400, message = "400 bad request, rest call is made with some invalid data!"),
                    @ApiResponse(code = 404, message = "404 not found, url is wrong")
            }
    )
    @PutMapping(value = "/updateTrainer/{trainerEmail}/trainerEmail",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,})
    private ResponseEntity updateTrainer(@RequestBody TrainerDto trainerDto,
                                        @PathVariable String trainerEmail) {

        Either<ErrorMsg, TrainerDto> updatedTrainer =
                trainerAdminService.updateTrainer(trainerDto, trainerEmail);
        return ResponseResolver.resolve(updatedTrainer);
    }
}

