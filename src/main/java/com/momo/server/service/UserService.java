package com.momo.server.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	// 로그인 메소드
	public User login(User user) {

		boolean isUserExist = false;

		isUserExist = userRepository.isUserExist(user);

		if (isUserExist) {
			return null;
		} else {
			createUser(user);
			return user;
		}
	}

	@Transactional
	// 유저 생성
	public void createUser(User user) {
		userRepository.createUser(user);
	}

	@Transactional(readOnly = true)
	// 희은님 부탁사항으로 만든 메소드
	public LinkedHashMap<String, LinkedHashMap<String, Boolean>> mapPlanList(User user) {

		LinkedHashMap<String, LinkedHashMap<String, Boolean>> planList = new LinkedHashMap<String, LinkedHashMap<String, Boolean>>();

		// 데이터 db에서 불러오기
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
		return planList;
	}

}
