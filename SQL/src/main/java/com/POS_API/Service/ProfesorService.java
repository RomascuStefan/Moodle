package com.POS_API.Service;

import com.POS_API.Advice.Exception.IdmServiceException;
import com.POS_API.Advice.Exception.RequestParamWrong;
import com.POS_API.DTO.ProfesorDTO;
import com.POS_API.Advice.Exception.ResourceNotFoundException;
import com.POS_API.Advice.Exception.UniqueKeyException;
import com.POS_API.Mapper.ProfesorMapper;
import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Profesor;
import com.POS_API.Repository.ProfesorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            throw new IdmServiceException(HttpStatus.FORBIDDEN,"Not a profesor");

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
}
