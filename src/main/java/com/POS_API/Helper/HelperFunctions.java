package com.POS_API.Helper;

import com.POS_API.Advice.Exception.RequestParamWrong;

import java.io.Console;
import java.io.PrintWriter;

public class HelperFunctions {
    public static int stringToInt(String s,String stringName) {
        int number;
        if (s == null || s.trim().isEmpty()) {
            throw new RequestParamWrong(stringName, s, "The parameter must not be null or empty");
        }
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new RequestParamWrong(stringName,s,"The parameter must be a valid integer");
        }

        return number;
    }
}
