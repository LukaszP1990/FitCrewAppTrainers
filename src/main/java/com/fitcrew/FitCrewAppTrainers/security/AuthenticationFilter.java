package com.fitcrew.FitCrewAppTrainers.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitcrew.FitCrewAppTrainers.dto.LoginDto;
import com.fitcrew.FitCrewAppTrainers.dto.TrainerDto;
import com.fitcrew.FitCrewAppTrainers.service.trainer.TrainerSignInService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final TrainerSignInService trainerSignInService;
	private final Environment environment;

	AuthenticationFilter(Environment environment,
						 AuthenticationManager authenticationManager,
						 TrainerSignInService trainerSignInService) {
		this.environment = environment;
		this.trainerSignInService = trainerSignInService;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {

		try {
			LoginDto cred = new ObjectMapper()
					.readValue(request.getInputStream(), LoginDto.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							cred.getEmail(),
							cred.getPassword(),
							new ArrayList<>()
					)
			);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {

		String email = ((User) auth.getPrincipal()).getUsername();
		TrainerDto trainerDetailsByEmail = trainerSignInService.getTrainerDetailsByEmail(email);

		String token = createJwtToken(trainerDetailsByEmail);

		setHeaderResponse(res, trainerDetailsByEmail, token);
	}

	private void setHeaderResponse(HttpServletResponse res, TrainerDto trainerDetailsByEmail, String token) {
		res.addHeader("token", token);
		res.addHeader("userId", trainerDetailsByEmail.getTrainerId());
	}

	private String createJwtToken(TrainerDto trainerDetailsByEmail) {
		return Jwts.builder()
				.setSubject(trainerDetailsByEmail.getTrainerId())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(
								Objects.requireNonNull(
										environment.getProperty("token.expiration_time")
								)
						)
				))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.compact();
	}
}
