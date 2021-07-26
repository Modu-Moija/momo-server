package com.momo.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.TimeSlot;
import com.momo.server.dto.MeetSubInfo;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.dto.response.MeetInfoRespDto;
import com.momo.server.exception.notfound.MeetNotFoundException;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.TimeSlotRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MeetService {

    private final MeetRepository meetRepository;
    private final TimeSlotRepository timeSlotRepository;

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

	int startNum = Integer.parseInt(start);
	int endNum = Integer.parseInt(end);
	int gap = requestDto.getGap();
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

	// meet로 저장
	meet.setMeetId(hashedUrl);
	meet.setTitle(requestDto.getTitle());
	meet.setStart(requestDto.getStart());
	meet.setEnd(requestDto.getEnd());
	meet.setCreated(LocalDateTime.now().plusHours(9));
	meet.setGap(requestDto.getGap());
	meet.setDates(dates);
	meet.setTimes(temptimes);
	meet.setCenter(requestDto.isCenter());
	meet.setCenter(requestDto.isVideo());
	meetRepository.createMeet(meet);

	createTimeSlot(hashedUrl, row, col, startNum, endNum, gap, dates);
	return ResponseEntity.ok().build();
    }

    @Transactional(readOnly = true)
    public MeetInfoRespDto getMeetInfo(String meetId) {
	MeetInfoRespDto meetInfoRespDto = new MeetInfoRespDto();

	Meet meetEntity = meetRepository.findMeet(meetId);

	Optional.ofNullable(meetEntity).orElseThrow(() -> new MeetNotFoundException(meetId));
	meetInfoRespDto.setCenter(meetEntity.isCenter());
	meetInfoRespDto.setVideo(meetEntity.isVideo());
	meetInfoRespDto.setTitle(meetEntity.getTitle());
	meetInfoRespDto.setDates(meetEntity.getDates());
	meetInfoRespDto.setStart(meetEntity.getStart());
	meetInfoRespDto.setEnd(meetEntity.getEnd());
	meetInfoRespDto.setGap(meetEntity.getGap());

	meetInfoRespDto.setMeetSubInfo(applyMeetSubInfo(meetEntity));

	return meetInfoRespDto;
    }

    // Timeslot 생성 메소드
    @Transactional
    public void createTimeSlot(String hashedUrl, int row, int col, int start, int end, int gap,
	    ArrayList<LocalDate> dates) {

	int totalMin = start * 60;

	ArrayList<TimeSlot> timeSlotList = new ArrayList<TimeSlot>();

	for (int i = 0; i < col; i++) {
	    int temp_Min = totalMin;
	    for (int j = 0; j < row; j++) {
		TimeSlot timeSlot = new TimeSlot();
		ArrayList<String> users = new ArrayList<String>();
		timeSlot.setMeetId(hashedUrl);
		timeSlot.setNum(0);
		timeSlot.setUsers(users);
		timeSlot.setDate(dates.get(i));
		timeSlot.setTime(String.valueOf(temp_Min / 60) + ":" + String.valueOf(temp_Min % 60));
		temp_Min = temp_Min + gap;

		timeSlotList.add(timeSlot);
	    }
	}
	timeSlotRepository.createTimeSlot(timeSlotList);
    }

    // meet정보 반환해줄 때 MeetSub 적용해주는 메소드
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
	meetSubInfo.setWho(meetEntity.getUserNames());

	return meetSubInfo;

    }

}
