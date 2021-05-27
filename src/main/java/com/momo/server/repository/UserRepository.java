package com.momo.server.repository;

import com.momo.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserRepository {

    //유저 생성 메소드
    void createUser(User user);

    boolean isUserExist(User user);

    void updateUserTimetable();

    void findUsername();
}
