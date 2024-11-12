package com.eyup.challenge.repository;

import com.eyup.challenge.model.Product;
import com.eyup.challenge.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    @Query("{ 'user.id': ?0, 'loggedOut': false }")
    List<Token> findAllAccessTokensByUser(String userId);

    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);
}