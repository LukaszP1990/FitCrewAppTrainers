package com.fitcrew.FitCrewAppTrainers.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

class TokenJwtUtil {

    private static final String AUTHORITIES_KEY = "roles";
    private static final String FULL_NAME = "fullName";
    private static final String USER_ID = "userId";

    private TokenJwtUtil() {
    }

    static String createToken(String secret) {

        long now = (new Date()).getTime();
        Date validity = new Date(now + 43200 * 1000);

        return Jwts.builder()
                .setSubject("test")
                .claim(USER_ID, 121)
                .claim(AUTHORITIES_KEY, "test")
                .claim(FULL_NAME, "test" + " " + "test")
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(validity)
                .compact();
    }
}
