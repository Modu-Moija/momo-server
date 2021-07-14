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
		public LinkedHashMap<String, LinkedHashMap<String, Boolean>> mapPlanList(User user) {
			
			LinkedHashMap<String, LinkedHashMap<String, Boolean>> planList = new LinkedHashMap<String, LinkedHashMap<String, Boolean>>();

			//데이터 db에서 불러오기
			User userEntity = userRepository.getUser(user.getUserId());
			Meet meetEntity = userRepository.getUserMeet(user.getMeetId());
			
			int[][] userTimes = userEntity.getUserTimes();
			String start = meetEntity.getStart();
			String end = meetEntity.getEnd();
			int gap = meetEntity.getGap();
			LocalDate startDate = meetEntity.getDates().get(0);
			int dayOfMonth = startDate.getDayOfMonth();
			
			int hour = Integer.parseInt(start.split(":")[0]);
			int gapTime = Integer.parseInt(start.split(":")[1]);
			int totalStartTime = hour*60+gapTime;
			//순서 저장을 위해 링크드해쉬맵 사용
			
			
			//이차원배열 출력해보는 테스트코드
//			for (int i = 0; i < userTimes[0].length; i++) {
//				for (int j = 0; j < userTimes.length; j++) {
//				System.out.print(userTimes[j][i] + " ");
//				}
//				System.out.println();
//				}
			//2차원 배열 돌면서 데이터 저장
			for(int i = 0;i<userTimes[0].length;i++) {
				Map<String, Boolean> time = new LinkedHashMap<String, Boolean>();
				dateTimeDto.setDate(String.valueOf(startDate.getYear())+"/"+String.valueOf(startDate.getMonthValue())+"/"+String.valueOf(dayOfMonth));
				//time = dateTimeDto.getTime()
				//String hourTime = String.valueOf(totalStartTime/60)+":"+String.valueOf(totalStartTime%60);
				System.out.println(userTimes.length);
				int temp_totalStartTime = totalStartTime;
				
				for(int j = 0;j<userTimes.length;j++) {
					System.out.print(userTimes[j][i]+ " ");
					//System.out.print("j :"+j+ " ");
					//System.out.print("hourtime :"+hourTime+ " ");
					if(userTimes[j][i]==0) {
						time.put(String.valueOf(temp_totalStartTime/60)+":"+String.valueOf(temp_totalStartTime%60), false);
					}else if(userTimes[j][i]==1) {
						System.out.println("실행됨?");
						//time.put(String.valueOf(temp_totalStartTime/60)+":"+String.valueOf(temp_totalStartTime%60), true);
					}
					temp_totalStartTime=temp_totalStartTime+gap;
				}
				System.out.println("분기");
				dateTimeDto.setTime(time);
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
