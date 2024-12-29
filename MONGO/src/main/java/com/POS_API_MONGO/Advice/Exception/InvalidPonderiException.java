package com.POS_API_MONGO.Advice.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidPonderiException extends RuntimeException{
    private final String sumaPonderi;

    @Override
    public String getMessage() {
        return String.format("Suma ponderilor (%s) nu este valida",sumaPonderi);
    }
}
