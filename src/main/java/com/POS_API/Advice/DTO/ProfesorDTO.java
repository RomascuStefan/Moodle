package com.POS_API.Advice.DTO;


import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Enums.TipAsociere;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfesorDTO {

    @NotBlank(message = "Numele nu poate fi gol.")
    private String nume;

    @NotBlank(message = "Prenumele nu poate fi gol.")
    private String prenume;

    @NotBlank(message = "Email-ul nu poate fi gol.")
    @Email(message = "Email-ul trebuie să fie valid.")
    private String email;

    @NotNull(message = "Gradul didactic este necesar.")
    private GradDidactic gradDidactic;

    @NotNull(message = "Tipul de asociere este necesar.")
    private TipAsociere tipAsociere;

    private String afiliere;
}

