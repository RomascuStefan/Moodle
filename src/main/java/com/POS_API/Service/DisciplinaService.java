package com.POS_API.Service;

import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Enums.CategorieDisciplina;
import com.POS_API.Model.Enums.TipDisciplina;
import com.POS_API.Model.Student;
import com.POS_API.Repository.DisciplinaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {

    private final DisciplinaDAO disciplinaRepo;

    @Autowired
    public DisciplinaService(DisciplinaDAO disciplinaRepo) {
        this.disciplinaRepo = disciplinaRepo;
    }

    public List<Disciplina> findAllDiscipline() {
        return disciplinaRepo.findAll();
    }

    public Optional<Disciplina> findDisciplinaByCod(String cod) {
        return disciplinaRepo.findDisciplinaByCod(cod);
    }

    public List<Disciplina> findDisciplinaByProfesorId(int profesorId) {
        return disciplinaRepo.findByTitular_Id(profesorId);
    }

    public List<Disciplina> filterDisciplineByType(List<Disciplina> discipline, String type) {
        try {
            TipDisciplina tipDisciplina = TipDisciplina.valueOf(type);

            return discipline.stream()
                    .filter(disciplina -> disciplina.getTipDisciplina() == tipDisciplina)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            System.out.println("Tip disciplina invalid: " + type);
            return null;
        }
    }

    public List<Disciplina> filterDisciplineByCategory(List<Disciplina> discipline, String category) {
        try {
            CategorieDisciplina categorieDisciplina = CategorieDisciplina.valueOf(category);

            return discipline.stream()
                    .filter(disciplina -> disciplina.getCategorieDisciplina() == categorieDisciplina)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            System.out.println("Categorie disciplina invalida: " + category);
            return null;
        }
    }

}
