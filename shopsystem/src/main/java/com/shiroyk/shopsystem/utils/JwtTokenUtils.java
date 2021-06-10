package com.shiroyk.shopsystem.utils;

import com.shiroyk.shopsystem.constant.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JwtTokenUtils {

    public static String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JwtConstants.SECRET)
                .compact();
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        Date expiredDate = getTokenBody(token).getExpiration();
        return expiredDate.before(new Date());
    }

    public static String getUsernameByToken(String token) {
        return getTokenBody(token).getSubject();
    }

}
