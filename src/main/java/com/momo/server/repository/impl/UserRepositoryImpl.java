package com.momo.server.repository.impl;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {


    private final MongoTemplate mongoTemplate;

    @Override
    public void createUser(User user) {
        BigInteger userid = BigInteger.valueOf(Integer.valueOf(Math.abs(user.hashCode())));
        user.setUserId(userid);
        mongoTemplate.insert(user, "user");

    }

    @Override
    public boolean isUserExist(User user) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));
        query.addCriteria(Criteria.where("username").is(user.getUsername()));

        return mongoTemplate.findOne(query,User.class, "user") != null;

    }

    @Override
    public void updateUserTimetable() {

    }

    @Override
    public void findUsername() {

    }

    @Override
    public void increaseMeetNum(String meetId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(meetId));
        Meet targetMeet = mongoTemplate.findOne(query, Meet.class, "meet");


    }


}
