package com.momo.server.repository.impl;

import com.momo.server.domain.User;
import com.momo.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void createUser() {

    }

    @Override
    public void createUserDatetime() {

    }
}
