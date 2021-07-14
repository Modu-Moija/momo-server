package com.momo.server.repository;


import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;

import java.util.ArrayList;

public interface MeetRepository {

    //약속생성 메소드
    void createMeet(Meet meet);
    
    boolean isMeetExist(String meetId);
    
    void addUser(String meetId, int userId);

	Meet getColorDate(String meetid);

	Meet findMeet(User user);

}
