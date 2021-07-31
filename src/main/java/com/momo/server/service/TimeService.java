package com.momo.server.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import com.momo.server.dto.auth.SessionUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.TimeSlot;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.exception.notfound.MeetNotFoundException;
import com.momo.server.exception.notfound.UserNotFoundException;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.TimeSlotRepository;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final UserRepository userRepository;
    private final MeetRepository meetRepository;
    private final TimeSlotRepository timeSlotRepository;

    /*
     * 유저시간 업데이트 및 약속시간 업데이트 메소드
     */
    @Transactional
    public ResponseEntity<?> updateUsertime(SessionUser userEntity, UserTimeUpdateRequestDto requestDto) {

	Optional.ofNullable(userEntity).orElseThrow(() -> new UserNotFoundException(userEntity.getUserId()));
	Meet meetEntity = meetRepository.findMeet(userEntity.getMeetId());
	Optional.ofNullable(meetEntity).orElseThrow(() -> new MeetNotFoundException(userEntity.getMeetId()));

	// dbmeet로 column 위치 계산
	ArrayList<LocalDate> dates = meetEntity.getDates();
	int[][] temp_userTimes = userEntity.getUserTimes();
	int[][] temp_Times = meetEntity.getTimes();
	int gap = meetEntity.getGap();
	String start = meetEntity.getStart();
	ArrayList<String> userNames = meetEntity.getUserNames();
	int userIndex=-1;
	if(userNames!=null){
		userIndex= this.checkUserIndexByName(userNames, userEntity.getUsername());
	}
	int hour = Integer.parseInt(start.substring(0, 2));
	int min = Integer.parseInt(start.substring(3, 5));
	int total_hour = hour * 60 + min;


	int col = 0;
	// db meet의 date로 날짜 찾기
	for (int i = 0; i < dates.size(); i++) {
	    // requestDto의 date 찾기
	    for (int j = 0; j < requestDto.getUsertimes().size(); j++) {
		if (requestDto.getUsertimes().get(j).getDate().equals(dates.get(i))) {
		    col = i;
		    // requestDto의 시간배열 크기만큼 반복
		    for (int t = 0; t < requestDto.getUsertimes().get(j).getTimeslots().size(); t++) {
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
			    // 1. 기존의 10진수를 2진수로 변환
				String temp_bin=Integer.toBinaryString(temp_Times[row][col]);
				// 2.
				// - 새로운 유저면 맨 뒤에 추가
				if(userIndex==-1){
					temp_bin=temp_bin+"1";
				} else{// - 기존 유저면 해당 인덱스의 이진수만 1
					temp_bin=temp_bin.substring(0,userIndex)+"1"+temp_bin.substring(userIndex+1);
				}

				// 3. 다시 2진수를 10진수로 변환해서 저장
			    temp_Times[row][col] = Integer.parseInt(temp_bin, 2);

			} else if (requestDto.getUsertimes().get(j).getTimeslots().get(t).getPossible() == false) {
			    temp_userTimes[row][col] = 0;
				String temp_bin=Integer.toBinaryString(temp_Times[row][col]);
				if(userIndex==-1){
					temp_bin=temp_bin+"0";
				} else{
					temp_bin=temp_bin.substring(0,userIndex)+"0"+temp_bin.substring(userIndex+1);
				}
				temp_Times[row][col] = Integer.parseInt(temp_bin, 2);
			}
		    }
		}
	    }
	}

	userRepository.updateUserTime(userEntity, temp_userTimes);

	// TimeSlot 갱신
	this.updateTimeSlot(userEntity, requestDto);
	this.addUserToMeet(meetEntity, userEntity);

	// Meet 시간 업데이트
	meetRepository.updateMeetTime(userEntity.getMeetId(), temp_Times);
	return ResponseEntity.ok().build();

    }

	@Transactional
	public void addUserToMeet(Meet meetEntity, SessionUser userEntity) {

		// userId 업데이트 연산
		ArrayList<BigInteger> userList = new ArrayList<BigInteger>();

		if (meetEntity.getUsers() == null) {
			userList.add(userEntity.getUserId());
		} else {
			userList = meetEntity.getUsers();
			if(checkUserIndexById(userList, userEntity.getUserId())==-1){//존재하지 않으면
				userList.add(userEntity.getUserId());
			}
		}

		// username 업데이트 연산
		ArrayList<String> userNameList = new ArrayList<String>();

		if (meetEntity.getUsers() == null) {
			userNameList.add(userEntity.getUsername());
		} else {
			userNameList = meetEntity.getUserNames();
			if(checkUserIndexByName(userNameList, userEntity.getUsername())==-1){//존재하지 않으면
				userNameList.add(userEntity.getUsername());
			}
		}
		meetRepository.addUser(meetEntity, userList, userNameList);
	}

	/*
	 * Meet의 usernames배열에서 username으로 인덱스 찾는 메소드
	 */
	private int checkUserIndexByName(ArrayList<?> userNames, String username) {
		System.out.println(userNames);
		System.out.println(username);
    	return userNames.indexOf(username);
	}

	/*
	 * Meet의 users배열에서 userId으로 인덱스 찾는 메소드
	 */
	private int checkUserIndexById(ArrayList<?> users, BigInteger userId) {
		return users.indexOf(userId);
	}


    /*
     * 유저시간 업데이트할 때 함께 TimeSlot 업데이트하는 메소드
     */
    @Transactional
    public void updateTimeSlot(SessionUser userEntity, UserTimeUpdateRequestDto requestDto) {

	List<TimeSlot> timeSlots = timeSlotRepository.findAllTimeSlot(requestDto.getMeetId());

	for (int i = 0; i < requestDto.getUsertimes().size(); i++) {
	    for (int j = 0; j < timeSlots.size(); j++) {
		if (requestDto.getUsertimes().get(i).getDate().equals(timeSlots.get(j).getDate())) {
		    for (int t = 0; t < requestDto.getUsertimes().get(i).getTimeslots().size(); t++) {
			if (requestDto.getUsertimes().get(i).getTimeslots().get(t).getTime()
				.equals(timeSlots.get(j).getTime())) {

			    HashSet<String> users = timeSlots.get(j).getUsers();
			    users.add(userEntity.getUsername());

			    timeSlotRepository.updateTimeSlot(requestDto.getMeetId(), users,
				    requestDto.getUsertimes().get(i).getDate(),
				    requestDto.getUsertimes().get(i).getTimeslots().get(t).getTime());
			}
		    }
		}
	    }
	}

    }

    /*
     * 약속관련정보 매핑하는 메소드
     */
    @Transactional(readOnly = true)
    public UserMeetRespDto mapUserMeetRespDto(SessionUser user) {

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
			String key = String.valueOf(temp_totalStartTime / 60) + ":" + String.valueOf(temp_totalStartTime % 60);
			if (userTimes[j][i] == 0) {
		    time.put(key, false);
		} else if (userTimes[j][i] == 1) {
		    time.put(key, true);
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

    @Transactional(readOnly = true)
    public List<TimeSlot> getLeastTime(String meetId) {

	List<TimeSlot> timeSlots = timeSlotRepository.findAllTimeSlot(meetId);

	Collections.sort(timeSlots, new Comparator<TimeSlot>() {

	    @Override
	    public int compare(TimeSlot t1, TimeSlot t2) {

		int res = t1.getNum().compareTo(t2.getNum());
		if (res == 0) {
		    res = t1.getDate().compareTo(t2.getDate());
		} else if (res == 0) {
		    res = t1.getTime().compareTo(t2.getTime());
		}
		// num순 정렬
		return res;
	    }

	});

	return timeSlots;

    }

    @Transactional(readOnly = true)
    public List<TimeSlot> getMostTime(String meetId) {

	List<TimeSlot> timeSlots = timeSlotRepository.findAllTimeSlot(meetId);

	Collections.sort(timeSlots, new Comparator<TimeSlot>() {

	    @Override
	    public int compare(TimeSlot t1, TimeSlot t2) {

		int res = t2.getNum().compareTo(t1.getNum());
		if (res == 0) {
		    res = t1.getDate().compareTo(t2.getDate());
		} else if (res == 0) {
		    res = t1.getTime().compareTo(t2.getTime());
		}
		// num순 정렬
		return res;
	    }

	});

	return timeSlots;

    }

}
