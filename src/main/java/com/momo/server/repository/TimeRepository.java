package com.momo.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TimeRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

}
