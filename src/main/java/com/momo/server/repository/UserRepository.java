package com.momo.server.repository;

import com.momo.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

}