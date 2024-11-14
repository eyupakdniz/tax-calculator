package com.eyup.demo.repository;

import com.eyup.demo.model.TaxLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaxLogRepository extends MongoRepository<TaxLog, String> {
}