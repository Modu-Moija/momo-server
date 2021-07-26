package com.momo.server.repository.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    @Override
    public List<TimeSlot> findAllTimeSlot(String meetId) {

	Query findAllTimeSlotQuery = new Query(Criteria.where("meetId").is(meetId));
	List<TimeSlot> timeslots = mongoTemplate.find(findAllTimeSlotQuery, TimeSlot.class);
	return timeslots;

    }

    @Override
    public void updateTimeSlot(String meetId, HashSet<String> users, LocalDate date, String time) {

	Query timeSlotFindQuery = new Query();
	timeSlotFindQuery.addCriteria(Criteria.where("meetId").is(meetId));
	timeSlotFindQuery.addCriteria(Criteria.where("date").is(date));
	timeSlotFindQuery.addCriteria(Criteria.where("time").is(time));

	Update timeSlotupdate = new Update();
	timeSlotupdate.inc("num", 1);
	timeSlotupdate.set("users", users);
	mongoTemplate.updateFirst(timeSlotFindQuery, timeSlotupdate, TimeSlot.class);

    }
}
