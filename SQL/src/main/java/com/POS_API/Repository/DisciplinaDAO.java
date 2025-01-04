package com.POS_API.Repository;

import com.POS_API.Model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaDAO extends JpaRepository<Disciplina,String> {
    Optional<Disciplina> findDisciplinaByCod(String cod);
    List<Disciplina> findByTitular_Id(int profesorId);
    int countByCodStartingWith(String codPrefix);
    Boolean existsByNumeDisciplinaAndAnStudiu(String nume, int an);
    Boolean existsByCod(String cod);
    Boolean existsByCodAndTitular_Email(String cod, String email);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM Disciplina d " +
            "JOIN d.studenti s " +
            "WHERE d.cod = :cod AND s.email = :email")
    boolean existsByCodAndStudentEmail(@Param("cod") String cod, @Param("email") String email);
}
