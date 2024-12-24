package com.POS_API_MONGO.Repository;


import com.POS_API_MONGO.Model.Materie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterieDAO extends MongoRepository<Materie, String> {
    Optional<Materie> findByCodMaterie(String codMaterie);
}
