package com.momo.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.repository.MeetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MeetService {


    private final MeetRepository meetRepository;


    //약속 생성메소드
    @Transactional
    public ResponseEntity<?> createMeet(MeetSaveRequestDto requestDto, String hashedUrl) {
    	
    	Meet meet = new Meet();
    	
        LocalDate startDate = requestDto.getDates().get(0);
        LocalDate endDate = requestDto.getDates().get(1);

        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
        LocalDate curDate = startDate;

        while (!curDate.equals(endDate.plusDays(1))) {
            dates.add(curDate);
            curDate=curDate.plusDays(1);
        }
        
        //meet의 times 이차원 배열 row 계산
        String start = requestDto.getStart().split(":")[0];
        String end = requestDto.getEnd().split(":")[0];

        int row = Integer.parseInt(end) - Integer.parseInt(start);
        row = (int)(60 / requestDto.getGap()) * row;
        
        
    	//meet의 times 이차원 배열 col 계산
        int col = dates.size();
        
        //meet의 times 이차원배열 0으로 채우기
        int[][] temptimes = new int[row][col];
        
        for (int i = 0; i < row; i++) { // 행 반복
			for (int j = 0; j < col; j++) { // 열 반복
				temptimes[i][j] = 0;
			}
		}
        
        //추후에 성능개선을 위해 캐시프랜들리 코드 적용 생각해보면 좋을 것 같음(행과 열 위치 연산개선)
        //참조링크 https://hot2364928.tistory.com/58
        
//        for (int i = 0; i < row; i++) { // 행 반복
//			for (int j = 0; j < col; j++) { // 열 반복
//				System.out.println(i+"행 "+j+"열의 값:"+temptimes[i][j]);
//			}
//		}

        //meet로 저장
        meet.setMeetId(hashedUrl);
        meet.setStart(requestDto.getStart());
        meet.setEnd(requestDto.getEnd());
        meet.setCreated(LocalDateTime.now().plusHours(9));
        meet.setGap(requestDto.getGap());
        meet.setDates(dates);
        //meet.setMeetSubInfo
        meet.setTimes(temptimes);
        meet.setCenter(requestDto.isCenter());
        meet.setCenter(requestDto.isVideo());
        meetRepository.createMeet(meet);
        return ResponseEntity.ok().build();
    }

    //'누구랑, 언제, 어디서' 생성하기 위한 메소드
    @Transactional
    public ResponseEntity<?> createMeetSub() {
        return ResponseEntity.ok().build();
    }
    
}


