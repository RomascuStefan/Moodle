package com.POS_API_MONGO.DTO;

import com.POS_API_MONGO.Model.POJO.Fisier;
import com.POS_API_MONGO.Model.POJO.Test;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MaterieDTO {
    @NotBlank(message = "Lipseste codul materiei")
    private String codMaterie;

    private List<Fisier> fisiereLaborator = new ArrayList<>();
    private List<Fisier> fisiereCurs = new ArrayList<>();
    private List<Test> probeExaminare = new ArrayList<>();
}
