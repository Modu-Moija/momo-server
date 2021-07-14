package com.momo.server.repository.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.repository.MeetRepository;

@Repository
public class MeetRepositoryImpl implements MeetRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void createMeet(Meet meet) {
        mongoTemplate.insert(meet, "meet");
    }
    
	@Override
    public Meet findMeet(User user) {

    	Meet dbmeet = mongoTemplate.findOne(Query.query(Criteria.where("meetId").is(user.getMeetId())), Meet.class);
        return dbmeet;
    }


    @Override
    public ArrayList<Integer> findUsers() {
        ArrayList users = new ArrayList();
        return users;
    }

    @Override
    public void addUser(String meetId, int userId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(meetId));
        Meet meet = mongoTemplate.findOne(query, Meet.class, "meet");
        Update update = new Update();
        update.inc("num", 1);
        mongoTemplate.updateFirst(query, update, "meet");


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

//	@Override
//	public void updateMeetTime(UserTimeUpdateRequestDto requestDto) {
//		
//		//DB에서 meet로 유저 찾기
//		Query query = new Query();
//		Meet dbmeet = mongoTemplate.findOne(Query.query(Criteria.where("meetid").is(requestDto.getMeetId())), Meet.class);
//		
//		
//	}
}
