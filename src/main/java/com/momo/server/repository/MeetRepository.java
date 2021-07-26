package com.momo.server.repository;

import java.math.BigInteger;
import java.util.ArrayList;

import com.momo.server.domain.Meet;

public interface MeetRepository {

    // 약속생성 메소드
    void createMeet(Meet meet);

    boolean isMeetExist(String meetId);

    Meet getColorDate(String meetid);

    Meet findMeet(String meetId);

    void addUser(Meet meetEntity, ArrayList<BigInteger> userList, ArrayList<String> userNameList);

    void findAllAndRemoveMeet(String meetId);

    void updateMeetTime(String meetId, int[][] temp_userTimes);

}
