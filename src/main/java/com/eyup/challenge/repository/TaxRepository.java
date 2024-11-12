package com.eyup.challenge.repository;

import com.eyup.challenge.model.Tax;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaxRepository extends MongoRepository<Tax, String> {
    Optional<Tax> findByName(String name);
}