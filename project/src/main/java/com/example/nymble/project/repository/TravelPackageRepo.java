package com.example.nymble.project.repository;

import com.example.nymble.project.model.TravelPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TravelPackageRepo extends MongoRepository<TravelPackage, String> {
}
