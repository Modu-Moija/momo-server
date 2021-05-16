package com.momo.server.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MeetRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;
}
