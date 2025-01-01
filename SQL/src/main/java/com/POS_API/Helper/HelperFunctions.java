package com.POS_API.Helper;

import auth.AuthServiceOuterClass;
import com.POS_API.Advice.Exception.IdmServiceException;
import com.POS_API.Advice.Exception.RequestParamWrong;
import org.springframework.http.HttpStatus;

public class HelperFunctions {

    public final static AuthServiceOuterClass.Role ADMIN = AuthServiceOuterClass.Role.admin;
    public final static AuthServiceOuterClass.Role PROFESOR = AuthServiceOuterClass.Role.profesor;
    public final static AuthServiceOuterClass.Role STUDENT = AuthServiceOuterClass.Role.student;



    public static int stringToInt(String s, String stringName) {
        int number;
        if (s == null || s.trim().isEmpty()) {
            throw new RequestParamWrong(stringName, s, "The parameter must not be null or empty");
        }
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new RequestParamWrong(stringName, s, "The parameter must be a valid integer");
        }

        return number;
    }

    public static String extractToken(String authorizationHeader) {
        final String errorMessage = "Authorization header is missing or invalid: ";

        if (authorizationHeader == null) {
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, errorMessage + "header is null");
        }
        if (authorizationHeader.trim().isEmpty()) {
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, errorMessage + "header is empty");
        }
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new IdmServiceException(HttpStatus.UNAUTHORIZED, errorMessage + "header must start with 'Bearer '");
        }

        return authorizationHeader.substring(7);
    }


}
