package com.momo.server.repository;


import com.momo.server.domain.User;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface MeetRepository {

    //약속생성 메소드
    void createMeet();


    ArrayList<Integer> findUsers();

    int[][] findTimes();
}
