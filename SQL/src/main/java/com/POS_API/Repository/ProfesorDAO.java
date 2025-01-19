
package com.POS_API.Repository;

import com.POS_API.Model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorDAO extends JpaRepository<Profesor, Integer> {
    List<Profesor> findByNumeStartingWithOrPrenumeStartingWith(String nume, String prenume);

    Boolean existsByEmail(String email);

    Optional<Profesor> findProfesorByEmail(String email);

}


