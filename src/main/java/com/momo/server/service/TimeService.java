package com.momo.server.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
	public ResponseEntity<?> updateUsertime(User user, UserTimeUpdateRequestDto requestDto) {
        
        userRepository.updateUserTime(user, requestDto);
        meetRepository.updateMeetTime();
        return ResponseEntity.ok().build();
		
	}
	
    
    
}
