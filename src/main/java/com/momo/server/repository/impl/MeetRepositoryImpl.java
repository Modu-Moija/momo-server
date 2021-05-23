package com.momo.server.repository.impl;

import com.momo.server.dto.MeetSaveRequestDto;
import com.momo.server.repository.MeetRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
}
