package com.fitcrew.FitCrewAppTrainers.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpEntityUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String SECRET = "hsgeqiwfknj243reaf4";

    public static HttpEntity<?> getHttpEntityWithJwt() {
        String jwt = TokenJwtUtil.createToken(SECRET);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(AUTHORIZATION_HEADER, BEARER.concat(jwt));
        return new HttpEntity<>(headers);
    }

    public static <T> HttpEntity<T> getHttpEntityWithJwtAndBody(T body) {
        String jwt = TokenJwtUtil.createToken(SECRET);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(AUTHORIZATION_HEADER, BEARER.concat(jwt));
        return new HttpEntity<>(body, headers);
    }
}
