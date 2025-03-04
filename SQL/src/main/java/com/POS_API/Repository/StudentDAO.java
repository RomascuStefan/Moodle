package com.POS_API.Repository;

import com.POS_API.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentDAO extends JpaRepository<Student,Integer> {

    Optional<Student> findStudentById(int id);
    Boolean existsByEmail(String email);
    Boolean existsByIdAndEmail(int id, String email);

    Optional<Student> findStudentByEmail(String email);

}
