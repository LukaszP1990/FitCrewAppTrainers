package com.fitcrew.FitCrewAppTrainers.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppTrainers.dto.LoginDto;
import com.fitcrew.FitCrewAppTrainers.resolver.ErrorMsg;
import com.fitcrew.FitCrewAppTrainers.service.trainer.signin.TrainerSignInService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vavr.control.Either;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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
            LoginDto loginDto = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword(),
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
                                            Authentication auth) {

        String email = ((User) auth.getPrincipal()).getUsername();
        Either<ErrorMsg, TrainerModel> trainerDetailsByEmail = trainerSignInService.getTrainerDetailsByEmail(email);

        trainerDetailsByEmail
                .map(this::createJwtToken)
                .peek(s -> setHeaderResponse(res, trainerDetailsByEmail.get(), s));
    }

    private void setHeaderResponse(HttpServletResponse res,
                                   TrainerModel trainerDetailsByEmail,
                                   String token) {
        res.addHeader("token", token);
        res.addHeader("userId", trainerDetailsByEmail.getTrainerId());
    }

    private String createJwtToken(TrainerModel trainerDetailsByEmail) {
        return Jwts.builder()
                .setSubject(trainerDetailsByEmail.getTrainerId())
                .setExpiration(new Date(getExpiration()))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();
    }

    private long getExpiration() {
        return System.currentTimeMillis() + Long.parseLong(
                Objects.requireNonNull(environment.getProperty("token.expiration_time"))
        );
    }
}
