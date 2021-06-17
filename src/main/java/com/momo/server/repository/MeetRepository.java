package com.momo.server.repository;


import com.momo.server.dto.MeetSaveRequestDto;
import java.util.ArrayList;

public interface MeetRepository {

    //약속생성 메소드
    void createMeet(MeetSaveRequestDto requestDto);

    int[][] findTimes();

    ArrayList<Integer> findUsers();
}
