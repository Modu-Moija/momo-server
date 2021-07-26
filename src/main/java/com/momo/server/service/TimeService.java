package com.momo.server.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.dto.response.MostLeastTimeRespDto;
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.exception.notfound.MeetNotFoundException;
import com.momo.server.exception.notfound.UserNotFoundException;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

    // 유저의 시간정보 저장
    @Transactional
    public ResponseEntity<?> updateUsertime(User userEntity, UserTimeUpdateRequestDto requestDto) {

	Optional.ofNullable(userEntity).orElseThrow(() -> new UserNotFoundException(userEntity.getUserId()));

	Meet meetEntity = meetRepository.findMeet(userEntity.getMeetId());
	Optional.ofNullable(meetEntity).orElseThrow(() -> new MeetNotFoundException(userEntity.getMeetId()));

	System.out.println(requestDto);

	// 유저 시간 업데이트
	// dbmeet로 column 위치 계산
	ArrayList<LocalDate> dates = meetEntity.getDates();

	// usertimetable 불러오기
	int[][] temp_userTimes = userEntity.getUserTimes();

	// meettimetable 불러오기
	int[][] temp_Times = meetEntity.getTimes();

	int gap = meetEntity.getGap();
	String start = meetEntity.getStart();
	int hour = Integer.parseInt(start.substring(0, 2));
	int min = Integer.parseInt(start.substring(3, 5));
	int total_hour = hour * 60 + min;

	int col = 0;
	// db meet의 date로 날짜 찾기
	for (int i = 0; i < dates.size(); i++) {
	    // requestDto의 date 찾기
	    for (int j = 0; j < requestDto.getUsertimes().size(); j++) {
		if (requestDto.getUsertimes().get(j).getDate().equals(dates.get(i))) {
		    System.out.println(requestDto.getUsertimes().get(j).getDate());
		    System.out.println(dates.get(i));
		    col = i;
		    // requestDto의 시간배열 크기만큼 반복

		    for (int t = 0; t < requestDto.getUsertimes().get(j).getTimeslots().size(); t++) {
			// 시간 설정
			String timeslot = requestDto.getUsertimes().get(j).getTimeslots().get(t).getTime();

			// dbmeet로 row 위치 계산
			int row = 0;

			int input_hour = Integer.parseInt(timeslot.substring(0, 2));
			int input_min = Integer.parseInt(timeslot.substring(3, 5));
			int input_total_hour = input_hour * 60 + input_min;

			row = (input_total_hour - total_hour) / gap;

			// true일 때 좌표값 1로 세팅,false일때 좌표값 0으로 세팅
			if (requestDto.getUsertimes().get(j).getTimeslots().get(t).getPossible() == true) {

			    temp_userTimes[row][col] = 1;
			    temp_Times[row][col] = temp_Times[row][col] + 1;
			} else if (requestDto.getUsertimes().get(j).getTimeslots().get(t).getPossible() == false) {
			    temp_userTimes[row][col] = 0;
			    temp_Times[row][col] = temp_Times[row][col] - 1;
			}
		    }
		}
	    }
	}

	userRepository.updateUserTime(userEntity, temp_userTimes, temp_Times);
//	// TimeSlot 갱신

	// Meet 시간 업데이트
	return ResponseEntity.ok().build();

    };

    public void updateMeetTime() {

    }

    /*
     * 약속관련정보 매핑하는 메소드
     */
    @Transactional(readOnly = true)
    public UserMeetRespDto mapUserMeetRespDto(User user) {

	UserMeetRespDto userMeetRespDto = new UserMeetRespDto();

	LinkedHashMap<String, LinkedHashMap<String, Boolean>> planList = new LinkedHashMap<String, LinkedHashMap<String, Boolean>>();

	// 데이터 db에서 불러오기
	User userEntity = userRepository.findUser(user);
	Optional.ofNullable(userEntity).orElseThrow(() -> new UserNotFoundException(user.getUserId()));

	Meet meetEntity = meetRepository.findMeet(user.getMeetId());
	Optional.ofNullable(meetEntity).orElseThrow(() -> new MeetNotFoundException(user.getMeetId()));

	int[][] userTimes = userEntity.getUserTimes();
	String start = meetEntity.getStart();
	String end = meetEntity.getEnd();
	int gap = meetEntity.getGap();
	LocalDate startDate = meetEntity.getDates().get(0);
	int dayOfMonth = startDate.getDayOfMonth();

	int hour = Integer.parseInt(start.split(":")[0]);
	int gapTime = Integer.parseInt(start.split(":")[1]);
	int totalStartTime = hour * 60 + gapTime;

	// 2차원 배열 돌면서 데이터 저장
	for (int i = 0; i < userTimes[0].length; i++) {

	    // 순서 저장을 위해 링크드해쉬맵 사용
	    LinkedHashMap<String, Boolean> time = new LinkedHashMap<String, Boolean>();

	    String temp_date = String.valueOf(startDate.getYear()) + "/" + String.valueOf(startDate.getMonthValue())
		    + "/" + String.valueOf(dayOfMonth);
	    int temp_totalStartTime = totalStartTime;

	    for (int j = 0; j < userTimes.length; j++) {
		if (userTimes[j][i] == 0) {
		    time.put(String.valueOf(temp_totalStartTime / 60) + ":" + String.valueOf(temp_totalStartTime % 60),
			    false);
		} else if (userTimes[j][i] == 1) {
		    time.put(String.valueOf(temp_totalStartTime / 60) + ":" + String.valueOf(temp_totalStartTime % 60),
			    true);
		}
		temp_totalStartTime = temp_totalStartTime + gap;
	    }
	    dayOfMonth++;
	    planList.put(temp_date, time);
	}

	userMeetRespDto.setMeetId(user.getMeetId());
	userMeetRespDto.setPlanList(planList);
	userMeetRespDto.setColorDate(getColorDate(meetEntity));

	return userMeetRespDto;
    }

    // 날짜 색깔 구하는 연산
    @Transactional(readOnly = true)
    public ArrayList<Integer> getColorDate(Meet meetEntity) {
	ArrayList<Integer> colorDate = new ArrayList<Integer>();

	int[][] times = meetEntity.getTimes();

	int temp = 0;
	for (int j = 0; j < times[0].length; j++) {
	    for (int i = 0; i < times.length; i++) {
		temp = temp + times[i][j];
	    }
	    colorDate.add(j, temp);
	    temp = 0;
	}
	return colorDate;
    }

    public MostLeastTimeRespDto getMostLeastTime(String meetId) {

	MostLeastTimeRespDto mostLeastTimeRespDto = new MostLeastTimeRespDto();
	Meet meetEntity = meetRepository.findMeet(meetId);

	mostLeastTimeRespDto.setLeast(null);
	mostLeastTimeRespDto.setMost(null);
	return mostLeastTimeRespDto;

    }

}
