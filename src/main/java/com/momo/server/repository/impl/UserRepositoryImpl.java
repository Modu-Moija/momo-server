package com.momo.server.repository.impl;

import com.momo.server.dto.auth.SessionUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.momo.server.domain.User;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void createUser(User user) {

	mongoTemplate.insert(user, "user");
    }

    @Override // 테스트코드를 위한 deleUser메소드 추가
    public void deleteUser() {
	mongoTemplate.remove(new Query(), "user");
    }

    @Override
    public User findUser(SessionUser user) {

	User userEntity = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(user.getUserId())), User.class);
	return userEntity;
    }

    @Override
    public User isUserExist(LoginRequestDto loginRequestDto) {

	Query query = new Query();
	query.addCriteria(Criteria.where("meetId").is(loginRequestDto.getMeetId()));
	query.addCriteria(Criteria.where("username").is(loginRequestDto.getUsername()));
	return mongoTemplate.findOne(query, User.class, "user");
    }

    // 유저시간 업데이트연산
    @Override
    public void updateUserTime(SessionUser user, int[][] temp_userTimes) {

	Query query = new Query(Criteria.where("userId").is(user.getUserId()));
	Update update = new Update();
	update.set("userTimes", temp_userTimes);
	mongoTemplate.updateFirst(query, update, User.class);
    }

    @Override
    public void findAndRemoveUser(String userId) {

	Query findUserQuery = new Query(Criteria.where("userId").is(userId));
	mongoTemplate.findAndRemove(findUserQuery, User.class);
    }

}
