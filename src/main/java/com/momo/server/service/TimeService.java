package com.momo.server.service;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;
import com.momo.server.utils.DateTimeDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {

	
    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

    
    //유저의 시간정보 저장
	public ResponseEntity<?> updateUsertime(User user, UserTimeUpdateRequestDto requestDto) {
        
        userRepository.updateUserTime(user, requestDto);
        //meetRepository.updateMeetTime(requestDto);
        return ResponseEntity.ok().build();
		
	};

	//날짜 색깔 구하는 연산
	public ArrayList getColorDate(String meetid) {
		ArrayList colorDate = new ArrayList();
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
