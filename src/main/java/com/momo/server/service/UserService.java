package com.momo.server.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

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
    public void createUser(User user) {// 유저 생성

	BigInteger userid = BigInteger.valueOf(Integer.valueOf(Math.abs(user.hashCode())));

	user.setUserId(userid);
	Meet meetEntity = meetRepository.findMeet(user.getMeetId());

	int dates = meetEntity.getDates().size();
	int timeslots = Integer.parseInt(meetEntity.getEnd().split(":")[0])
		- Integer.parseInt(meetEntity.getStart().split(":")[0]);
	int[][] userTimes = new int[timeslots * ((int) 60 / meetEntity.getGap())][dates];
	user.setUserTimes(userTimes);

	meetRepository.addUser(meetEntity, userid);
	userRepository.createUser(user);
    }

}
