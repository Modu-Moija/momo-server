package com.momo.server.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void createUser(User user) {

	mongoTemplate.insert(user, "user");
    }

    @Override
    public User findUser(User user) {

	User userEntity = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(user.getUserId())), User.class);
	return userEntity;
    }

    @Override
    public boolean isUserExist(User user) {

	Query query = new Query();
	query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));
	query.addCriteria(Criteria.where("username").is(user.getUsername()));
	return mongoTemplate.findOne(query, User.class, "user") != null;
    }

    // 유저시간 업데이트연산
    @Override
    public void updateUserTime(User user, int[][] temp_userTimes, int[][] temp_Times) {

	Query query = new Query(Criteria.where("userId").is(user.getUserId()));
	Update update = new Update();
	update.set("userTimes", temp_userTimes);
	mongoTemplate.updateFirst(query, update, User.class);

	Query meetQuery = new Query(Criteria.where("meetId").is(user.getMeetId()));
	Update meetUpdate = new Update();
	meetUpdate.set("times", temp_Times);
	mongoTemplate.updateFirst(meetQuery, meetUpdate, Meet.class);
    }

    @Override
    public void increaseMeetNum(String meetId) {

	Query query = new Query();
	query.addCriteria(Criteria.where("meetId").is(meetId));
	Meet targetMeet = mongoTemplate.findOne(query, Meet.class, "meet");
    }

}
