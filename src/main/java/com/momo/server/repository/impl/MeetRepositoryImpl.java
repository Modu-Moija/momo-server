package com.momo.server.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.momo.server.domain.Meet;
import com.momo.server.repository.MeetRepository;

@Repository
public class MeetRepositoryImpl implements MeetRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void createMeet(Meet meet) {
	mongoTemplate.insert(meet, "meet");
    }

    @Override // 테스트코드를 위한 deleteMeet메소드 추가
    public void findAllAndRemoveMeet(String meetId) {

	Query findMeetQuery = new Query(Criteria.where("meetId").is(meetId));
	mongoTemplate.findAllAndRemove(findMeetQuery, Meet.class);
    }

    @Override
    public Meet findMeet(String meetId) {

	Meet meetEntity = mongoTemplate.findOne(Query.query(Criteria.where("meetId").is(meetId)), Meet.class);
	return meetEntity;
    }

    // Meet에 user 추가하는연산
    @Override
    public void addUser(Meet meetEntity, ArrayList<BigInteger> userList, ArrayList<String> userNameList) {

	Query addUserQuery = new Query(Criteria.where("meetId").is(meetEntity.getMeetId()));
	Update addUserUpdate = new Update();
	addUserUpdate.set("users", userList);
	addUserUpdate.set("userNames", userNameList);
	addUserUpdate.inc("num", 1);
	mongoTemplate.updateFirst(addUserQuery, addUserUpdate, Meet.class);
    }

    @Override
    public boolean isMeetExist(String meetId) {

	Query query = new Query();
	Meet existing_meet;

	query.addCriteria(Criteria.where("meetId").is(meetId));
	existing_meet = mongoTemplate.findOne(query, Meet.class, "meet");

	return existing_meet != null;
    }

    @Override
    public Meet getColorDate(String meetid) {
	Query query = new Query();
	Meet meet;

	query.addCriteria(Criteria.where("meetId").is(meetid));
	meet = mongoTemplate.findOne(query, Meet.class, "meet");

	return meet;
    }

    @Override
    public void updateMeetTime(String meetId, int[][] temp_Times) {

	Query meetQuery = new Query(Criteria.where("meetId").is(meetId));
	Update meetUpdate = new Update();
	meetUpdate.set("times", temp_Times);
	meetUpdate.set("times", temp_Times);
	mongoTemplate.updateFirst(meetQuery, meetUpdate, Meet.class);

    }
}
