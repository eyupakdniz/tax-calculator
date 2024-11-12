package com.eyup.challenge.repository;

import com.eyup.challenge.model.Product;
import com.eyup.challenge.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository  extends MongoRepository<Product, String> {
    Optional<Product> findByName(String name);
    List<Product> findByUser(User user);

}
