package com.POS_API.Controller;

import com.POS_API.Model.Disciplina;
import com.POS_API.Service.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/academia/lectures")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    @Autowired
    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public List<Disciplina> findAllDiscipline(@RequestParam(required = false) String type, @RequestParam(required = false) String category)
    {
        List<Disciplina> discipline = disciplinaService.findAllDiscipline();

        if(type != null){
            discipline=disciplinaService.filterDisciplineByType(discipline,type);
        }

        if(category != null){
            discipline=disciplinaService.filterDisciplineByCategory(discipline,category);
        }

        if(discipline == null){
            //TODO
        }

        return discipline;
    }

    @GetMapping("/{cod}")
    public Optional<Disciplina> findDisciplinaByCod(@PathVariable String cod)
    {
        return disciplinaService.findDisciplinaByCod(cod);
    }


}



