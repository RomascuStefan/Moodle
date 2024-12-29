package com.POS_API_MONGO.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterieFilesResponseDTO {
    private List<String> fisiereLaborator;
    private List<String> fisiereCurs;
}
