package com.POS_API_MONGO.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddFileResponseDTO {
    private String fileName;
    private String location;

    public String getMessage() {
        return String.format("File '%s' added in: '%s'", fileName, location);
    }

}
