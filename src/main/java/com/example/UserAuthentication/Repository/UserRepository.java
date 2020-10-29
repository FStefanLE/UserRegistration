package com.example.UserAuthentication.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.UserAuthentication.Model.MongoUser;

@Repository
public interface UserRepository extends MongoRepository<MongoUser, String> {

}
