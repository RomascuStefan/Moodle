
package com.POS_API.Repository;

import com.POS_API.Model.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProfesorDAO extends JpaRepository<Profesor,Integer>
{
    Optional <Profesor> findProfesorById(int id);
    List<Profesor> findByNumeStartingWithOrPrenumeStartingWith(String nume, String prenume);

}


