package com.fitcrew.FitCrewAppTrainers.resolver;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseResolver {

	public static ResponseEntity resolve(Either<? extends ErrorMsg, ?> either) {
		return either
				.map(ResponseEntity::ok)
				.getOrElseGet(ResponseResolver::createResponseError);
	}

	private static ResponseEntity createResponseError(ErrorMsg error) {
		log.debug("Error: {}", error);
		return ResponseEntity.badRequest().body(error);
	}
}
