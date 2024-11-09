package com.POS_API.Model;

import com.POS_API.Model.Enums.CategorieDisciplina;
import com.POS_API.Model.Enums.TipDisciplina;
import com.POS_API.Model.Enums.TipExaminare;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "discipline")
@Getter
@Setter
@ToString
public class Disciplina {

    @Id
    private String cod;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_titular", nullable = true)
    private Profesor titular;

    @Column(name = "nume_disciplina", nullable = false, length = 100)
    private String numeDisciplina;

    @Column(name = "an_studiu", nullable = false)
    private int anStudiu;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_disciplina", nullable = false)
    private TipDisciplina tipDisciplina;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie_disciplina", nullable = false)
    private CategorieDisciplina categorieDisciplina;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_examinare", nullable = false)
    private TipExaminare tipExaminare;

    @ManyToMany(mappedBy = "discipline")
    @JsonIgnore
    private List<Student> studenti;

}
