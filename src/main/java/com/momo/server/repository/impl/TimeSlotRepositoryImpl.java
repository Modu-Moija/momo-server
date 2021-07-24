package com.momo.server.repository.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.momo.server.domain.TimeSlot;
import com.momo.server.repository.TimeSlotRepository;

@Repository
public class TimeSlotRepositoryImpl implements TimeSlotRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void createTimeSlot(ArrayList<TimeSlot> timeSlotList) {

	mongoTemplate.insertAll(timeSlotList);

    }

    @Override // 테스트코드를 위한 deleteMeet메소드 추가
    public void findAllAndRemoveTimeSlot(String meetId) {

	Query findTimeSlotQuery = new Query(Criteria.where("meetId").is(meetId));
	mongoTemplate.findAllAndRemove(findTimeSlotQuery, TimeSlot.class);
    }

}
