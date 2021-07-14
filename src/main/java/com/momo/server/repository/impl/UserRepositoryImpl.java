package com.momo.server.repository.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {


    private final MongoTemplate mongoTemplate;

    @Transactional
    @Override
    public void createUser(User user) {
        BigInteger userid = BigInteger.valueOf(Integer.valueOf(Math.abs(user.hashCode())));
        
        
        user.setUserId(userid);
        Meet meet = getUserMeet(user.getMeetId());
        
        
        int dates = meet.getDates().size();
        int timeslots = Integer.parseInt(meet.getEnd().split(":")[0]) - Integer.parseInt(meet.getStart().split(":")[0]);
        int[][] userTimes = new int[timeslots*((int) 60/meet.getGap())][dates];
        user.setUserTimes(userTimes);
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
    public User findUser(User user) {

    	User dbuser = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(user.getUserId())), User.class);
        return dbuser;
    }

	@Override
    public void updateUserTime(User user, int[][] temp_userTimes,  int[][] temp_Times) {
		
		//DB에서 user 찾기
		Query query = new Query(Criteria.where("userId").is(user.getUserId()));
		Update update = new Update();
	    update.set("userTimes", temp_userTimes);
	    mongoTemplate.updateFirst(query, update, User.class);
	   
	    
		//DB에서 meet 찾기
	    Query meetQuery = new Query(Criteria.where("meetId").is(user.getMeetId()));
		Update meetUpdate = new Update();
		meetUpdate.set("times", temp_Times);
	    mongoTemplate.updateFirst(meetQuery, meetUpdate, Meet.class);
	    
		
    }

	@Transactional
    @Override
    public void increaseMeetNum(String meetId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(meetId));
        Meet targetMeet = mongoTemplate.findOne(query, Meet.class, "meet");
    }

    //유저가 속한 Meet 불러오는 메소드
    @Transactional(readOnly = true)
    @Override
    public Meet getUserMeet(String meetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(meetId));
        return mongoTemplate.findOne(query, Meet.class, "meet");
    }
    
    
    //유저아이디로 User 객체 불러오는 메소드
    @Transactional(readOnly = true)
	@Override
    public User getUser(BigInteger userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, User.class, "user");
    }


}
