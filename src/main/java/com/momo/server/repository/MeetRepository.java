package com.momo.server.repository;

import java.math.BigInteger;

import com.momo.server.domain.Meet;

public interface MeetRepository {

    // 약속생성 메소드
    void createMeet(Meet meet);

    boolean isMeetExist(String meetId);

    void addUser(Meet meet, BigInteger userid);

    Meet getColorDate(String meetid);

    Meet findMeet(String meetId);

    void deleteMeet();

}
