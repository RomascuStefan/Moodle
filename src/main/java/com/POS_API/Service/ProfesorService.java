package com.POS_API.Service;

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
        return profesorRepo.findAll();
    }

    public Optional<Profesor> findProfesorById(int id) {
        return profesorRepo.findProfesorById(id);
    }

    public List<Profesor> filterProfesoriByRank(List<Profesor> profesori, String acad_rank) {
        try {
            GradDidactic gradDidactic = GradDidactic.valueOf(acad_rank);

            return profesori.stream()
                    .filter(profesor -> profesor.getGradDidactic() == gradDidactic)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            //TODO
            System.out.println("Grad didactic invalid: " + acad_rank);
            return null;
        }


    }

    public List<Profesor> findProfesoriByName(String nume) {
        return profesorRepo.findByNumeStartingWithOrPrenumeStartingWith(nume,nume);
    }
}
