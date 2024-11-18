package com.POS_API.Helper;

import com.POS_API.Advice.Exception.RequestParamWrong;

public class HelperFunctions {
    public static int stringToInt(String s,String stringName) {
        int number;
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new RequestParamWrong(stringName,s,"The parameter must be a valid integer");
        }

        return number;
    }
}
