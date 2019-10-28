package com.legendApi.security.helpers;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;

import java.util.Map;

public class CurrentUser {
    public static  Map<String, ?> GetClaims() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

        JsonParser parser = JsonParserFactory.getJsonParser();
        return parser.parseMap(JwtHelper.decode(token).getClaims());
    }

    public static long getId() {
        return Long.parseLong(GetClaims().get("userId").toString());
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
}
