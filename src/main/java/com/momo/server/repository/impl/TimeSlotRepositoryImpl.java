package com.momo.server.repository.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

}
