package com.momo.server.repository.impl;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.exception.MeetDoesNotExistException;
import com.momo.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

        if (!isMeetExist(user.getMeetId()))
            throw new MeetDoesNotExistException();

        query.addCriteria(Criteria.where("userId").is(user.getUsername()));
        queryUser = mongoTemplate.findOne(query,User.class, "user");

        return queryUser != null;

    }

    @Override
    public void updateUserTimetable() {

    }

    @Override
    public void findUsername() {

    }

    @Override
    public boolean isMeetExist(String meetId) {

        Query query = new Query();
        Meet existing_meet;

        query.addCriteria(Criteria.where("meetId").is(meetId));
        existing_meet = mongoTemplate.findOne(query, Meet.class, "meet");

        return existing_meet != null;
    }

}
