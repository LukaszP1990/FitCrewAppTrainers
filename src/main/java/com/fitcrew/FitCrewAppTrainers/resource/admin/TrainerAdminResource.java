package com.fitcrew.FitCrewAppTrainers.resource.admin;

import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ResponseResolver;
import com.fitcrew.FitCrewAppTrainers.service.admin.TrainerAdminServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Resource for admin web service")
@Slf4j
@RestController
@RequestMapping("/trainer")
class TrainerAdminResource {

    private final TrainerAdminServiceFacade trainerAdminServiceFacade;

    TrainerAdminResource(TrainerAdminServiceFacade trainerAdminServiceFacade) {
        this.trainerAdminServiceFacade = trainerAdminServiceFacade;
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
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getTrainers() {

        return ResponseResolver.resolve(trainerAdminServiceFacade.getTrainers());
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
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deleteTrainer(@PathVariable String trainerEmail) {
        log.debug("Delete trainer by trainer email address: {}", trainerEmail);

        return ResponseResolver.resolve(trainerAdminServiceFacade.deleteTrainer(trainerEmail));
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
                    MediaType.APPLICATION_XML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateTrainer(@RequestBody TrainerDto trainerDto,
                                        @PathVariable String trainerEmail) {
        log.debug("Update trainer: {} \n by trainer email address: {}", trainerDto, trainerEmail);

        return ResponseResolver.resolve(
                trainerAdminServiceFacade.updateTrainer(trainerDto, trainerEmail)
        );
    }

    @ApiOperation(value = "Return single trainer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful get single trainer response!"),
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

        return ResponseResolver.resolve(trainerAdminServiceFacade.getTrainer(trainerEmail));
    }

}

