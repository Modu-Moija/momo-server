package com.momo.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.dto.MeetSubInfo;
import com.momo.server.dto.MeetSummary;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.dto.response.MeetInfoRespDto;
import com.momo.server.repository.MeetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MeetService {

    private final MeetRepository meetRepository;

    // 약속 생성메소드
    @Transactional
    public ResponseEntity<?> createMeet(MeetSaveRequestDto requestDto, String hashedUrl) {

	Meet meet = new Meet();

	LocalDate startDate = requestDto.getDates().get(0);
	LocalDate endDate = requestDto.getDates().get(1);

	ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
	LocalDate curDate = startDate;

	while (!curDate.equals(endDate.plusDays(1))) {
	    dates.add(curDate);
	    curDate = curDate.plusDays(1);
	}

	// meet의 times 이차원 배열 row 계산
	String start = requestDto.getStart().split(":")[0];
	String end = requestDto.getEnd().split(":")[0];

	int row = Integer.parseInt(end) - Integer.parseInt(start);
	row = (int) (60 / requestDto.getGap()) * row;

	// meet의 times 이차원 배열 col 계산
	int col = dates.size();

	// meet의 times 이차원배열 0으로 채우기
	int[][] temptimes = new int[row][col];

	for (int i = 0; i < row; i++) { // 행 반복
	    for (int j = 0; j < col; j++) { // 열 반복
		temptimes[i][j] = 0;
	    }
	}

	// 추후에 성능개선을 위해 캐시프랜들리 코드 적용 생각해보면 좋을 것 같음(행과 열 위치 연산개선)
	// 참조링크 https://hot2364928.tistory.com/58

	// 테스트코드
//        for (int i = 0; i < row; i++) { // 행 반복
//			for (int j = 0; j < col; j++) { // 열 반복
//				System.out.println(i+"행 "+j+"열의 값:"+temptimes[i][j]);
//			}
//		}

	// meet로 저장
	meet.setMeetId(hashedUrl);
	meet.setTitle(requestDto.getTitle());
	meet.setStart(requestDto.getStart());
	meet.setEnd(requestDto.getEnd());
	meet.setCreated(LocalDateTime.now().plusHours(9));
	meet.setGap(requestDto.getGap());
	meet.setDates(dates);
	// meet.setMeetSubInfo
	meet.setTimes(temptimes);
	meet.setCenter(requestDto.isCenter());
	meet.setCenter(requestDto.isVideo());
	meetRepository.createMeet(meet);
	return ResponseEntity.ok().build();
    }

    public MeetInfoRespDto getMeetInfo(String meetId) {
	MeetInfoRespDto meetInfoRespDto = new MeetInfoRespDto();
	MeetSummary meetSummary = new MeetSummary();

	Meet meetEntity = meetRepository.findMeet(meetId);

	meetSummary.setCenter(meetEntity.isCenter());
	meetSummary.setVideo(meetEntity.isVideo());
	meetSummary.setTitle(meetEntity.getTitle());
	meetSummary.setDates(meetEntity.getDates());
	meetSummary.setStart(meetEntity.getStart());
	meetSummary.setEnd(meetEntity.getEnd());
	meetSummary.setGap(meetEntity.getGap());

	meetSummary.setMeetSubInfo(applyMeetSubInfo(meetEntity));
	meetInfoRespDto.setSummary(meetSummary);

	return meetInfoRespDto;
    }

    public MeetSubInfo applyMeetSubInfo(Meet meetEntity) {

	MeetSubInfo meetSubInfo = new MeetSubInfo();

	meetSubInfo.setWhen(
		meetEntity.getDates().get(0) + " ~ " + meetEntity.getDates().get(meetEntity.getDates().size() - 1));

	String where = null;
	if (meetEntity.isVideo() == true) {
	    where = "화상회의";
	} else {
	    where = "대면회의";
	}
	meetSubInfo.setWhere(where);

	ArrayList<String> who = new ArrayList<String>();
	who.add("조은학");
	who.add("호날두");
	who.add("메시");

	meetSubInfo.setWho(who);

	return meetSubInfo;

    }

}
