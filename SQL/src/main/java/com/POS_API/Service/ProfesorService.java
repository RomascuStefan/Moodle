package com.POS_API.Service;

import com.POS_API.Advice.Exception.*;
import com.POS_API.DTO.ProfesorDTO;
import com.POS_API.DTO.ProfesorPatchDTO;
import com.POS_API.Mapper.ProfesorMapper;
import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Enums.TipAsociere;
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

    public List<ProfesorDTO> findAllProfesori() {
        List<Profesor> profesori = profesorRepo.findAll();

        return ProfesorMapper.listEntityToDTO(profesori);
    }

    public ProfesorDTO findProfesorById(int id) {
        Optional<Profesor> profesor = profesorRepo.findById(id);
        
        if (profesor.isEmpty()) {
            throw new ResourceNotFoundException("Profesor", "id", id);
        }
        return ProfesorMapper.toDTO(profesor.get());
    }

    public ProfesorDTO findProfesorByEmail(String email) {
        Optional<Profesor> profesor = profesorRepo.findProfesorByEmail(email);

        if (profesor.isEmpty())
            throw new UnprocesableEntityException("Not a profesor");

        return ProfesorMapper.toDTO(profesor.get());
    }

    public List<ProfesorDTO> filterProfesoriByRank(List<ProfesorDTO> profesoriDTO, String acad_rank) {
        try {
            GradDidactic gradDidactic = GradDidactic.valueOf(acad_rank);

            List<Profesor> profesori = ProfesorMapper.listDTOToEntity(profesoriDTO);

            List<Profesor> filteredProfesori = profesori.stream()
                    .filter(profesor -> profesor.getGradDidactic() == gradDidactic)
                    .collect(Collectors.toList());

            return ProfesorMapper.listEntityToDTO(filteredProfesori);
        } catch (IllegalArgumentException e) {
            throw new RequestParamWrong("acad_rank",acad_rank,"gradul didactic este invalid");
        }
    }

    public List<ProfesorDTO> findProfesoriByName(String nume) {
        List<Profesor> profesori = profesorRepo.findByNumeStartingWithOrPrenumeStartingWith(nume, nume);

        return ProfesorMapper.listEntityToDTO(profesori);
    }

    public ProfesorDTO addProfesor(ProfesorDTO profesorDTO) {
        Profesor profesor = ProfesorMapper.toEntity(profesorDTO);

        if (profesorRepo.existsByEmail(profesor.getEmail())) {
            throw new UniqueKeyException("Profesori", profesor.getEmail());
        }

        return ProfesorMapper.toDTO(profesorRepo.save(profesor));
    }

    public List<ProfesorDTO> filterProfesoriByAnStudii(List<ProfesorDTO> profesori, String anStudii) {
        try {
            int an = Integer.parseInt(anStudii);

            if (an > 4 || an < 1)
                throw new IllegalArgumentException("year must be between 1 and 4.");

            List<Profesor> profesorEntity = ProfesorMapper.listDTOToEntity(profesori);

            List<Profesor> filteredProfesori = profesorEntity.stream()
                    .filter(profesor -> profesor.getDiscipline() != null)
                    .filter(profesor -> profesor.getDiscipline().stream()
                            .anyMatch(disciplina -> disciplina.getAnStudiu() == an)) // Compară anul
                    .collect(Collectors.toList());

            return ProfesorMapper.listEntityToDTO(filteredProfesori);


        } catch (NumberFormatException ex) {
            throw new RequestParamWrong("anStudii", anStudii, "year must be a valid integer.");
        } catch (IllegalArgumentException ex) {
            throw new RequestParamWrong("anStudii", anStudii, ex.getMessage());
        }
    }

    public ProfesorDTO patchProfesor(int id, ProfesorPatchDTO profesorPatchDTO) {
        Optional<Profesor> optionalProfesor = profesorRepo.findById(id);

        if (optionalProfesor.isEmpty()) {
            throw new ResourceNotFoundException("Profesor", "id", id);
        }

        Profesor profesor = optionalProfesor.get();

        if (profesorPatchDTO.getNume() != null && !profesorPatchDTO.getNume().isEmpty()) {
            profesor.setNume(profesorPatchDTO.getNume());
        }

        if (profesorPatchDTO.getPrenume() != null && !profesorPatchDTO.getPrenume().isEmpty()) {
            profesor.setPrenume(profesorPatchDTO.getPrenume());
        }

        if (profesorPatchDTO.getGradDidactic() != null && !profesorPatchDTO.getGradDidactic().isEmpty()) {
            try {
                GradDidactic gradDidactic = GradDidactic.valueOf(profesorPatchDTO.getGradDidactic());
                profesor.setGradDidactic(gradDidactic);
            } catch (IllegalArgumentException e) {
                throw new EnumException("grad didactic", profesorPatchDTO.getGradDidactic());
            }
        }

        if (profesorPatchDTO.getTipAsociere() != null && !profesorPatchDTO.getTipAsociere().isEmpty()) {
            try {
                TipAsociere tipAsociere = TipAsociere.valueOf(profesorPatchDTO.getTipAsociere());
                profesor.setTipAsociere(tipAsociere);
            } catch (IllegalArgumentException e) {
                throw new EnumException("tip asociere", profesorPatchDTO.getTipAsociere());
            }
        }

        if (profesorPatchDTO.getAfiliere() != null && !profesorPatchDTO.getAfiliere().isEmpty()) {
            profesor.setAfiliere(profesorPatchDTO.getAfiliere());
        }

        Profesor updatedProfesor = profesorRepo.save(profesor);

        return ProfesorMapper.toDTO(updatedProfesor);
    }

    public boolean existByEmailAndId(String email, int id){
        return profesorRepo.existsByEmailAndId(email,id);
    }


}
