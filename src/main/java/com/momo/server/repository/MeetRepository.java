package com.momo.server.repository;

import com.momo.server.domain.Meet;

public interface MeetRepository {

    // 약속생성 메소드
    void createMeet(Meet meet);

    boolean isMeetExist(String meetId);

    void addUser(String meetId, int userId);

    Meet getColorDate(String meetid);

    Meet findMeet(String meetId);

}
