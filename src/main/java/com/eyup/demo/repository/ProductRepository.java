package com.eyup.demo.repository;

import com.eyup.demo.model.Product;
import com.eyup.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findByUser(User user);

}
