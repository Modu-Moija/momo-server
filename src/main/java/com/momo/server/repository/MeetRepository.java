package com.momo.server.repository;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;

public interface MeetRepository {

    // 약속생성 메소드
    void createMeet(Meet meet);

    boolean isMeetExist(String meetId);

    void addUser(Meet meet, User user);

    Meet getColorDate(String meetid);

    Meet findMeet(String meetId);

    void deleteMeet();

}
