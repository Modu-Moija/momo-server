package com.momo.server.repository.impl;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.exception.MeetDoesNotExistException;
import com.momo.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {


    private final MongoTemplate mongoTemplate;

    @Override
    public int createUser(User user) {
        return 0;
    }

    @Override
    public boolean isUserExist(User user) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));
        query.addCriteria(Criteria.where("userId").is(user.getUsername()));

        return mongoTemplate.findOne(query,User.class, "user") != null;

    }

    @Override
    public void updateUserTimetable() {

    }

    @Override
    public void findUsername() {

    }



}
