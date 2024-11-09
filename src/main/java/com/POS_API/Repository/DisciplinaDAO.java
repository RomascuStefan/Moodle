package com.POS_API.Repository;

import com.POS_API.Model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaDAO extends JpaRepository<Disciplina,String> {
    Optional<Disciplina> findDisciplinaByCod(String cod);
    List<Disciplina> findByTitular_Id(int profesorId);
}
