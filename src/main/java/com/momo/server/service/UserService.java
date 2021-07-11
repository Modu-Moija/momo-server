package com.momo.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;
import com.momo.server.utils.DateTimeDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

    //로그인 메소드
    public User login(User user) {

        boolean isUserExist = false;

        isUserExist = userRepository.isUserExist(user);

        if (isUserExist){
            return null;
        }else{
            createUser(user);
            return user;
        }
    }

    //유저 생성
    public void createUser(User user) {
        userRepository.createUser(user);
    }
    
    
    
    //나중에 코드좀더 쉽게 할수도 있을듯
		public ArrayList<DateTimeDto> mapPlanList(User user) {
			
			ArrayList<DateTimeDto> planList = new ArrayList<DateTimeDto>();
			
			
			User userEntity = userRepository.getUser(user.getUserId());
			Meet meetEntity = userRepository.getUserMeet(user.getMeetId());
			
			
			int[][] userTimes = userEntity.getUserTimes();
			LocalDateTime created = meetEntity.getCreated();
			String start = meetEntity.getStart();
			String end = meetEntity.getEnd();
			int gap = meetEntity.getGap();
			int dayOfMonth = created.getDayOfMonth();
			
			
			int hour = Integer.parseInt(start.split(":")[0]);
			int gapTime = Integer.parseInt(start.split(":")[1]);
			int totalStartTime = hour*60+gapTime;
			
			for(int i = 0;i<userTimes[0].length;i++) {
				DateTimeDto dateTimeDto = new DateTimeDto();
				
				dateTimeDto.setDate(String.valueOf(created.getYear())+"/"+String.valueOf(created.getMonthValue())+"/"+String.valueOf(dayOfMonth));
				Map<String, Boolean> time = dateTimeDto.getTime();
				
				for(int j=0;j<userTimes.length;j++) {
					
					String hourTime = String.valueOf(totalStartTime/60)+":"+String.valueOf(totalStartTime%60);
					
					if(userTimes[i][j]==0) {
						time.put(hourTime, false);
					}else {
						time.put(hourTime, true);
					}
				}
				dateTimeDto.setTime(time);
				
				totalStartTime=totalStartTime+gap;
				dayOfMonth++;
				planList.add(dateTimeDto);
			}
			return planList;
			
		}

	


//    //유저이름 찾는 메소드
//    public ArrayList findUsername() {
//        userRepository.findUsername();
//        return null;
//    }
}
