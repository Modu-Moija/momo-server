package com.momo.server.repository.impl;

import com.momo.server.domain.Meet;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.repository.MeetRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MeetRepositoryImpl implements MeetRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void createMeet(MeetSaveRequestDto requestDto) {
        mongoTemplate.insert(requestDto, "meet");
    }

    @Override
    public int[][] findTimes() {
        return new int[0][];
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
}
