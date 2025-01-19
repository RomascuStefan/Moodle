package com.POS_API_MONGO.Advice.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileNameExistsException extends RuntimeException{
    private String filename;
    private String location;

    @Override
    public String getMessage() {
        return String.format("Fisierul %s exista deja in %s",filename,location);
    }
}
