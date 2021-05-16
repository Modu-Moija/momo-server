package com.momo.server.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;
}
