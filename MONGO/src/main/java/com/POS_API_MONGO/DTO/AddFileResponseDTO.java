package com.POS_API_MONGO.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class AddFileResponseDTO {
    private String fileName;
    private String location;

    @Override
    public String toString() {
        return String.format("File '%s' added in: '%s'", fileName, location);
    }

}
