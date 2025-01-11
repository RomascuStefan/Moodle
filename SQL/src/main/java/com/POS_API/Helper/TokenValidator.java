package com.POS_API.Helper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TokenValidator {
    private static LocalDate savedDate;
    private static String jwt="";

    public static boolean isTokenValid() {
        if (jwt.trim().isEmpty())
            return false;

        LocalDate now = LocalDate.now();
        if (ChronoUnit.HOURS.between(savedDate.atStartOfDay(), now.atStartOfDay()) >= 7)
            return false;

        return true;
    }

    public static void setToken(String newJwt) {
        jwt = newJwt;
        savedDate = LocalDate.now();
    }

    public static String getToken() {
        return jwt;
    }

}
