package com.eyup.demo.repository;

import com.eyup.demo.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaxRepository extends JpaRepository<Tax, Long> {

    Optional<Tax> findByName(String name);

}
