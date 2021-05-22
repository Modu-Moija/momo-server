package com.momo.server.repository.impl;

import com.momo.server.domain.User;
import com.momo.server.repository.MeetRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MeetRepositoryImpl implements MeetRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void createMeet() {

    }

    @Override
    public int[][] findTimes() {
        return new int[0][];
    }

    @Override
    public Optional<User> getUsers() {
        return null;
    }
}
