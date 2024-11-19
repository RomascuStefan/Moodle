package com.POS_API.Service;

import com.POS_API.Advice.Exception.RequestParamWrong;
import com.POS_API.Advice.Exception.ResourceNotFoundException;
import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.Mapper.DisciplinaMapper;
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

    public List<DisciplinaDTO> findAllDiscipline() {
        return DisciplinaMapper.listToDTO(disciplinaRepo.findAll());
    }

    public DisciplinaDTO findDisciplinaByCod(String cod) {
        Optional<Disciplina> disciplina = disciplinaRepo.findDisciplinaByCod(cod);
        if (disciplina.isEmpty()) {
            throw new ResourceNotFoundException("Disciplina", "cod disciplina", cod);
        }

        return DisciplinaMapper.toDTO(disciplina.get());
    }

    public List<DisciplinaDTO> findDisciplinaByProfesorId(int profesorId) {
        List<Disciplina> disciplinaList = disciplinaRepo.findByTitular_Id(profesorId);

        return DisciplinaMapper.listToDTO(disciplinaList);
    }

    public List<DisciplinaDTO> filterDisciplineByType(List<DisciplinaDTO> disciplineDTO, String type) {
        try {
            TipDisciplina tipDisciplina = TipDisciplina.valueOf(type);

        } catch (IllegalArgumentException e) {
            throw new RequestParamWrong("tip discilpina", type, "tipul disciplinei este invalid");
        }

        return disciplineDTO.stream()
                .filter(disciplinaDTO -> disciplinaDTO.getTipDisciplina().equals(type))
                .collect(Collectors.toList());
    }

    public List<DisciplinaDTO> filterDisciplineByCategory(List<DisciplinaDTO> discipline, String category) {
        try {
            CategorieDisciplina categorieDisciplina = CategorieDisciplina.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new RequestParamWrong("tip discilpina", category, "categoria disciplinei este invalid");
        }

        return discipline.stream()
                .filter(disciplinaDTO -> disciplinaDTO.getCategorieDisciplina().equals(category))
                .collect(Collectors.toList());
    }

    public int getNumarOrdine(String cod)
    {
        return disciplinaRepo.countByCodStartingWith(cod);
    }

}
