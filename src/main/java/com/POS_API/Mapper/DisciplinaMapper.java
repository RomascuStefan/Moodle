package com.POS_API.Mapper;

import com.POS_API.Advice.Exception.EnumException;
import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.Helper.HelperFunctions;
import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Enums.CategorieDisciplina;
import com.POS_API.Model.Enums.TipDisciplina;
import com.POS_API.Model.Enums.TipExaminare;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaMapper {

    private static String getDisciplinaCod(DisciplinaDTO disciplinaDTO, int nrOrdin) {
        String cod = "";

        String[] words = disciplinaDTO.getNumeDisciplina().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            initials.append(word.charAt(0));
        }

        String initialeUpperCase = initials.toString().toUpperCase();

        cod = disciplinaDTO.getAnStudiu() + initialeUpperCase + (nrOrdin + 1);

        return cod;
    }

    public static Disciplina toEntity(DisciplinaDTO disciplinaDTO, int nrOrdin) {
        int anStudiu = HelperFunctions.stringToInt(disciplinaDTO.getAnStudiu(), "an studiu");

        if (anStudiu < 1 || anStudiu > 4)
            throw new IllegalArgumentException("year must be between 1 and 4.");

        TipDisciplina tipDisciplina;
        try {
            tipDisciplina = TipDisciplina.valueOf(disciplinaDTO.getTipDisciplina());
        } catch (IllegalArgumentException e) {
            throw new EnumException("tip disciplina", disciplinaDTO.getTipDisciplina());
        }

        CategorieDisciplina categorieDisciplina;
        try {
            categorieDisciplina = CategorieDisciplina.valueOf(disciplinaDTO.getCategorieDisciplina());
        } catch (IllegalArgumentException e) {
            throw new EnumException("categorie disciplina", disciplinaDTO.getCategorieDisciplina());
        }

        TipExaminare tipExaminare;
        try {
            tipExaminare = TipExaminare.valueOf(disciplinaDTO.getTipExaminare());
        } catch (IllegalArgumentException e) {
            throw new EnumException("tip examinare", disciplinaDTO.getTipExaminare());
        }

        String cod = getDisciplinaCod(disciplinaDTO, nrOrdin);


        Disciplina disciplina = new Disciplina();


        disciplina.setCod(cod);
        disciplina.setNumeDisciplina(disciplinaDTO.getNumeDisciplina());
        disciplina.setAnStudiu(anStudiu);
        disciplina.setTipDisciplina(tipDisciplina);
        disciplina.setCategorieDisciplina(categorieDisciplina);
        disciplina.setTipExaminare(tipExaminare);
        disciplina.setStudenti(disciplinaDTO.getStudenti());

        return disciplina;
    }

    public static DisciplinaDTO toDTO(Disciplina disciplina) {
        DisciplinaDTO disciplinaDTO = new DisciplinaDTO();

        disciplinaDTO.setCod(disciplina.getCod());
        disciplinaDTO.setAnStudiu(Integer.toString(disciplina.getAnStudiu()));
        disciplinaDTO.setNumeDisciplina(disciplina.getNumeDisciplina());
        disciplinaDTO.setTipDisciplina(disciplina.getTipDisciplina().toString());
        disciplinaDTO.setCategorieDisciplina(disciplina.getCategorieDisciplina().toString());
        disciplinaDTO.setTipExaminare(disciplina.getTipExaminare().toString());
        disciplinaDTO.setStudenti(disciplina.getStudenti());

        return disciplinaDTO;
    }

    public static List<DisciplinaDTO> listToDTO(List<Disciplina> disciplinaEntityList) {
        List<DisciplinaDTO> disciplinaDTOList = new ArrayList<>();

        for (Disciplina disciplina : disciplinaEntityList) {
            disciplinaDTOList.add(toDTO(disciplina));
        }

        return disciplinaDTOList;
    }
}
