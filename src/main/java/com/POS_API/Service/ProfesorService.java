package com.POS_API.Service;

import com.POS_API.Advice.Exception.RequestParamWrong;
import com.POS_API.Advice.Exception.ResourceNotFoundException;
import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Profesor;
import com.POS_API.Repository.ProfesorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfesorService {

    private final ProfesorDAO profesorRepo;

    @Autowired
    public ProfesorService(ProfesorDAO profesorRepo) {
        this.profesorRepo = profesorRepo;
    }

    public List<Profesor> findAllProfesori() {
        List<Profesor> profesori = profesorRepo.findAll();
        if (profesori.isEmpty()) {
            throw new ResourceNotFoundException("Profesor", "all", "no records found");
        }
        return profesori;
    }

    public Profesor findProfesorById(int id) {
        Optional<Profesor> profesor = profesorRepo.findById(id);
        if (profesor.isEmpty()) {
            throw new ResourceNotFoundException("Profesor", "id", id);
        }
        return profesor.get();
    }

    public List<Profesor> filterProfesoriByRank(List<Profesor> profesori, String acad_rank) {
        try {
            GradDidactic gradDidactic = GradDidactic.valueOf(acad_rank);

            List<Profesor> filteredProfesori = profesori.stream()
                    .filter(profesor -> profesor.getGradDidactic() == gradDidactic)
                    .collect(Collectors.toList());

            if (filteredProfesori.isEmpty()) {
                throw new ResourceNotFoundException("Profesor", "gradDidactic", acad_rank);
            }

            return filteredProfesori;
        } catch (IllegalArgumentException e) {
            throw new RequestParamWrong("acad_rank", acad_rank);
        }
    }

    public List<Profesor> findProfesoriByName(String nume) {
        List<Profesor> profesori = profesorRepo.findByNumeStartingWithOrPrenumeStartingWith(nume, nume);

        if (profesori.isEmpty()) {
            throw new ResourceNotFoundException("Profesor", "name like", nume);
        }

        return profesori;
    }
}
