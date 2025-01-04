package com.POS_API.Helper;

import auth.AuthServiceOuterClass;
import com.POS_API.Advice.Exception.RequestParamWrong;

public class HelperFunctions {

    public final static AuthServiceOuterClass.Role ADMIN = AuthServiceOuterClass.Role.admin;
    public final static AuthServiceOuterClass.Role PROFESOR = AuthServiceOuterClass.Role.profesor;
    public final static AuthServiceOuterClass.Role STUDENT = AuthServiceOuterClass.Role.student;
    public final static AuthServiceOuterClass.Role SQL = AuthServiceOuterClass.Role.sql;
    public final static AuthServiceOuterClass.Role MONGO = AuthServiceOuterClass.Role.mongo;



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
}
