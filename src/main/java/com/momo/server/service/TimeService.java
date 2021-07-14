package com.momo.server.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {

	
    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

    
    //유저의 시간정보 저장
    @Transactional
	public ResponseEntity<?> updateUsertime(User user, UserTimeUpdateRequestDto requestDto) {
		
		User dbuser = userRepository.findUser(user);
		Meet dbmeet = meetRepository.findMeet(user);

		//dbmeet로 column 위치 계산
		ArrayList<LocalDate> dates = dbmeet.getDates();
		
		int x=0;
		for(int i =0; i<dates.size();i++) {
//			System.out.println("요청 "+requestDto.getDate());
//			System.out.println("db날짜 "+dates.get(i));
			if(requestDto.getDate().equals(dates.get(i))) {
				//System.out.println(i);
				x=i;
				break;
			}
		}
		
		//dbmeet로 row 위치 계산
		int y=0;
		String start = dbmeet.getStart();
		int hour = Integer.parseInt(start.substring(0,2));
		int min = Integer.parseInt(start.substring(3,5));
		int total_hour = hour*60+min;
		String timeslot = requestDto.getTimeslot();
		int input_hour = Integer.parseInt(timeslot.substring(0,2));
		int input_min = Integer.parseInt(timeslot.substring(3,5));
		int input_total_hour = input_hour*60+input_min;
		int gap = dbmeet.getGap();
		
		y=(input_total_hour-total_hour)/gap;
		
		
		//usertimetable 불러오기
		int[][] temp_userTimes = dbuser.getUserTimes();
		
		//meettimetable 불러오기
		int[][] temp_Times = dbmeet.getTimes();
		
		//true일 때 좌표값 1로 세팅,false일때 좌표값 0으로 세팅
		if(requestDto.isPossible()==true) {
			temp_userTimes[y][x]=1;
			temp_Times[y][x]=temp_Times[y][x]+1;
		}
		else if(requestDto.isPossible()==false) {
			temp_userTimes[y][x]=0;
			temp_Times[y][x]=temp_Times[y][x]-1;
		}
        
        userRepository.updateUserTime(user, temp_userTimes, temp_Times);
        
        return ResponseEntity.ok().build();
		
	};
	
	//날짜 색깔 구하는 연산
	@Transactional(readOnly = true)
	public ArrayList<Integer> getColorDate(String meetid) {
		ArrayList<Integer> colorDate = new ArrayList<Integer>();
		Meet meet = meetRepository.getColorDate(meetid);
		
		int[][] times = meet.getTimes();
		
		int temp=0;
		for(int j =0; j<times[0].length;j++) {
			for(int i =0; i<times.length;i++) {
				temp=temp+times[i][j];
			}
			colorDate.add(j, temp);
			temp=0;
		}
		return colorDate;
	}
	
	
	
    
    
}
