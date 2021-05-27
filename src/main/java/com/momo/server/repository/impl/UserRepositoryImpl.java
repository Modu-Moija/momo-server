package com.momo.server.repository.impl;

import com.momo.server.domain.User;
import com.momo.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void createUser(User user) {

    }

    @Override
    public boolean isUserExist(User user) {

        Query query = new Query();
        User queryUser;

        query.addCriteria(Criteria.where("userId").is(user.getUserId()));
        query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));
        queryUser = mongoTemplate.findOne(query,User.class, "user");

        if( queryUser == null) return false;

        return true;

    }

    @Override
    public void updateUserTimetable() {

    }

    @Override
    public void findUsername() {

    }

}
