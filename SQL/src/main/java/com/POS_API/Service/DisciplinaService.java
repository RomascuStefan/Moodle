package com.POS_API.Service;

import com.POS_API.Advice.Exception.*;
import com.POS_API.DTO.DisciplinaDTO;
import com.POS_API.DTO.MongoAddLectureDTO;
import com.POS_API.DTO.ProfesorDTO;
import com.POS_API.Mapper.DisciplinaMapper;
import com.POS_API.Mapper.ProfesorMapper;
import com.POS_API.Model.Disciplina;
import com.POS_API.Model.Enums.CategorieDisciplina;
import com.POS_API.Model.Enums.TipDisciplina;
import com.POS_API.Model.Profesor;
import com.POS_API.Repository.DisciplinaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {

    private final DisciplinaDAO disciplinaRepo;
    private final RestTemplate restTemplate;

    @Value("${mongo.service.url}")
    private String mongoServiceUrl;

    @Autowired
    public DisciplinaService(DisciplinaDAO disciplinaRepo, RestTemplate restTemplate) {
        this.disciplinaRepo = disciplinaRepo;
        this.restTemplate = restTemplate;
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

    private int getNumarOrdin(String cod) {
        return disciplinaRepo.countByCodStartingWith(cod);
    }

    @Transactional(rollbackFor = Exception.class)
    public DisciplinaDTO addDisciplina(String header, DisciplinaDTO disciplinaDTO, ProfesorDTO titularDTO) {

        String codPrefix = DisciplinaMapper.getPrefix(disciplinaDTO);

        int nrOrdin = getNumarOrdin(codPrefix);

        Profesor titular = ProfesorMapper.toEntity(titularDTO);
        Disciplina disciplina = DisciplinaMapper.toEntity(disciplinaDTO, nrOrdin, titular);

        if (disciplinaRepo.existsByNumeDisciplinaAndAnStudiu(disciplina.getNumeDisciplina(), disciplina.getAnStudiu())) {
            throw new UniqueKeyException("Disciplina", disciplina.getNumeDisciplina() + " predat in anul de studii: " + disciplina.getAnStudiu());
        }

        MongoAddLectureDTO requestBody = new MongoAddLectureDTO();
        requestBody.setCodMaterie(disciplina.getCod());
        requestBody.setExaminare(disciplina.getTipExaminare().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", header);

        HttpEntity<MongoAddLectureDTO> requestEntity = new HttpEntity<>(requestBody, headers);

        String url = mongoServiceUrl + "/add";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> mongoResponse = restTemplate.postForEntity(url, requestEntity, String.class);

        if (mongoResponse.getStatusCode() != HttpStatus.CREATED) {
            throw new MongoServiceException(mongoResponse.getStatusCode(), mongoResponse.getBody());
        }

        return DisciplinaMapper.toDTO(disciplinaRepo.save(disciplina));
    }


    public boolean isTeaching(String cod, String email) {
        if (disciplinaRepo.existsByCodAndTitular_Email(cod, email))
            return true;

        if(!disciplinaRepo.existsByCod(cod))
            throw new ResourceNotFoundException("Disciplina","cod",cod);

        throw new IdmServiceException(HttpStatus.FORBIDDEN, "Access forbidden");
    }

    public boolean isAttending(String cod, String email) {
        if (disciplinaRepo.existsByCodAndStudentEmail(cod, email))
            return true;

        if(!disciplinaRepo.existsByCod(cod))
            throw new ResourceNotFoundException("Disciplina","cod",cod);

        throw new IdmServiceException(HttpStatus.FORBIDDEN, "Access forbidden");
    }
}
