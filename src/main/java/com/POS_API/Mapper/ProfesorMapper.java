package com.POS_API.Mapper;

import com.POS_API.Advice.Exception.EnumException;
import com.POS_API.DTO.ProfesorDTO;
import com.POS_API.Model.Enums.GradDidactic;
import com.POS_API.Model.Enums.TipAsociere;
import com.POS_API.Model.Profesor;

import java.util.ArrayList;
import java.util.List;

public class ProfesorMapper {

    public static ProfesorDTO toDTO(Profesor profesor) {
        ProfesorDTO profesorDTO = new ProfesorDTO();

        profesorDTO.setId(profesor.getId());
        profesorDTO.setNume(profesor.getNume());
        profesorDTO.setPrenume(profesor.getPrenume());
        profesorDTO.setEmail(profesor.getEmail());
        profesorDTO.setGradDidactic(profesor.getGradDidactic().toString());
        profesorDTO.setTipAsociere(profesor.getTipAsociere().toString());
        profesorDTO.setAfiliere(profesor.getAfiliere());
        profesorDTO.setDiscipline(profesor.getDiscipline());

        return profesorDTO;

    }

    public static Profesor toEntity(ProfesorDTO profesorDTO) {
        Profesor profesor = new Profesor();

        GradDidactic gradDidactic;
        TipAsociere tipAsociere;
        try {
            gradDidactic = GradDidactic.valueOf(profesorDTO.getGradDidactic());
        } catch (IllegalArgumentException e) {
            throw new EnumException("grad didactic", profesorDTO.getGradDidactic());
        }

        try {
            tipAsociere = TipAsociere.valueOf(profesorDTO.getTipAsociere());
        } catch (IllegalArgumentException e) {
            throw new EnumException("tip asociere", profesorDTO.getTipAsociere());
        }

        profesor.setId(profesorDTO.getId());
        profesor.setNume(profesorDTO.getNume());
        profesor.setPrenume(profesorDTO.getPrenume());
        profesor.setEmail(profesorDTO.getEmail());
        profesor.setGradDidactic(gradDidactic);
        profesor.setTipAsociere(tipAsociere);
        profesor.setAfiliere(profesorDTO.getAfiliere());
        profesor.setDiscipline(profesorDTO.getDiscipline());

        return profesor;
    }

    public static List<ProfesorDTO> listEntityToDTO(List<Profesor> profesori) {
        List<ProfesorDTO> listProfesoriDTO = new ArrayList<>();

        for (Profesor profesor : profesori)
            listProfesoriDTO.add(toDTO(profesor));

        return listProfesoriDTO;
    }

    public static List<Profesor> listDTOToEntity(List<ProfesorDTO> profesoriDTO) {
        List<Profesor> listProfesori = new ArrayList<>();

        for (ProfesorDTO profesorDTO : profesoriDTO)
            listProfesori.add(toEntity(profesorDTO));


        return listProfesori;
    }

}
