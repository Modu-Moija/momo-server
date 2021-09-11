package com.momo.server.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import com.momo.server.exception.outofbound.UsersOutOfBoundsException;
import com.momo.server.utils.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.exception.notfound.MeetNotFoundException;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

    @Autowired
    public UserService(UserRepository userRepository, MeetRepository meetRepository) {
        this.userRepository = userRepository;
        this.meetRepository = meetRepository;
    }

    // 로그인 메소드
    @Transactional
    public User login(LoginRequestDto loginRequestDto) {

        User userEntity = userRepository.isUserExist(loginRequestDto);
        if (userEntity == null) {// 유저 존재하지않음(신규유저)
            userEntity = this.createUser(loginRequestDto);
            return userEntity;
        } else {// 유저 존재(기존 유저)
            return userEntity;
        }
    }

    // 유저 생성
    @Transactional
    public User createUser(LoginRequestDto loginRequestDto) {

        Meet meetEntity = meetRepository.findMeet(loginRequestDto.getMeetId());
        validateMeetId(loginRequestDto, meetEntity);
        validateUserCount(loginRequestDto, meetEntity);
        int[][] userTimes = getUserTimes(meetEntity);

        User user = new User();
        setUser(loginRequestDto, userTimes, user);
        userRepository.createUser(user);

        this.addUserToMeet(meetEntity, user);
        return user;
    }

    private void setUser(LoginRequestDto loginRequestDto, int[][] userTimes, User user) {
        BigInteger userid = BigInteger.valueOf(
            Integer.valueOf(Math.abs(loginRequestDto.hashCode())));
        user.setUserId(userid);
        user.setCreated(CurrentTime.getCurrentTime());
        user.setUsername(loginRequestDto.getUsername());
        user.setCookieRemember(loginRequestDto.getRemember());
        user.setMeetId(loginRequestDto.getMeetId());
        user.setUserTimes(userTimes);
    }

    private int[][] getUserTimes(Meet meetEntity) {
        int dates = meetEntity.getDates().size();
        int timeslots = Integer.parseInt(meetEntity.getEnd().split(":")[0])
            - Integer.parseInt(meetEntity.getStart().split(":")[0]);
        int[][] userTimes = new int[timeslots * ((int) 60 / meetEntity.getGap())][dates];
        return userTimes;
    }

    private void validateMeetId(LoginRequestDto loginRequestDto, Meet meetEntity) {
        //없는 meetId에 대한 예외처리
        Optional.ofNullable(meetEntity)
            .orElseThrow(() -> new MeetNotFoundException(loginRequestDto.getMeetId()));
    }

    private void validateUserCount(LoginRequestDto loginRequestDto, Meet meetEntity) {

        //인원수 초과에 대한 예외처리
        if (meetEntity.getUsers() != null) {
            if (meetEntity.getUsers().size() == 100) {
                throw new UsersOutOfBoundsException(loginRequestDto.getMeetId());
            }
        }
    }

    /*
    user meet에 추가
     */
    @Transactional
    public void addUserToMeet(Meet meetEntity, User userEntity) {

        ArrayList<BigInteger> userList = new ArrayList<>();
        userList = updateUserIdList(meetEntity, userEntity, userList);

        ArrayList<String> userNameList = new ArrayList<>();
        userNameList = updateUserNameList(meetEntity, userEntity, userNameList);
        meetRepository.addUser(meetEntity, userList, userNameList);
    }

    /*
        username 업데이트 연산
     */
    private ArrayList<String> updateUserNameList(Meet meetEntity, User userEntity,
        ArrayList<String> userNameList) {
        if (meetEntity.getUsers() == null) {
            userNameList.add(userEntity.getUsername());
        } else {
            userNameList = meetEntity.getUserNames();
            if (userNameList.indexOf(userEntity.getUsername()) == -1) {//존재하지 않으면
                userNameList.add(userEntity.getUsername());
            }
        }
        return userNameList;
    }

    /*
        userId 업데이트 연산
     */
    private ArrayList<BigInteger> updateUserIdList(Meet meetEntity, User userEntity,
        ArrayList<BigInteger> userList) {
        if (meetEntity.getUsers() == null) {
            userList.add(userEntity.getUserId());
        } else {
            userList = meetEntity.getUsers();
            if (userList.indexOf(userEntity.getUserId()) == -1) {//존재하지 않으면
                userList.add(userEntity.getUserId());
            }
        }
        return userList;
    }


}
