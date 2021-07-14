package com.momo.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    
    
    
    //희은님 부탁사항으로 만든 메소드
		public ArrayList<DateTimeDto> mapPlanList(User user) {
			
			ArrayList<DateTimeDto> planList = new ArrayList<DateTimeDto>();
			
			
			User userEntity = userRepository.getUser(user.getUserId());
			Meet meetEntity = userRepository.getUserMeet(user.getMeetId());
			
			//데이터 db에서 불러오기
			int[][] userTimes = userEntity.getUserTimes();
			String start = meetEntity.getStart();
			String end = meetEntity.getEnd();
			int gap = meetEntity.getGap();
			
			LocalDate startDate = meetEntity.getDates().get(0);
			
			int dayOfMonth = startDate.getDayOfMonth();
			
			
			int hour = Integer.parseInt(start.split(":")[0]);
			int gapTime = Integer.parseInt(start.split(":")[1]);
			int totalStartTime = hour*60+gapTime;
			Map<String, Boolean> time = new LinkedHashMap<String, Boolean>();//순서 저장을 위해 링크드해쉬맵 사용
			
			
			//2차원 배열 돌면서 데이터 저장
			for(int i = 0;i<userTimes[0].length;i++) {
				DateTimeDto dateTimeDto = new DateTimeDto();
				
				dateTimeDto.setDate(String.valueOf(startDate.getYear())+"/"+String.valueOf(startDate.getMonthValue())+"/"+String.valueOf(dayOfMonth));
				//time = dateTimeDto.getTime();
				
				for(int j=0;j<userTimes.length;j++) {
					
					String hourTime = String.valueOf(totalStartTime/60)+":"+String.valueOf(totalStartTime%60);
					
					if(userTimes[j][i]==0) {
						time.put(hourTime, false);
					}else if(userTimes[j][i]==1) {
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
