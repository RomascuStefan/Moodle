package com.POS_API.Model;

import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Enums.TipAsociere;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "profesori")
@Getter
@Setter
@ToString
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String nume;

    @Column(nullable = false, length = 50)
    private String prenume;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "grad_didactic", nullable = false)
    private GradDidactic gradDidactic;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_asociere", nullable = false)
    private TipAsociere tipAsociere;

    @Column(length = 100)
    private String afiliere;

    @OneToMany(mappedBy = "titular")
    @JsonIgnore
    private List<Disciplina> discipline;
}




