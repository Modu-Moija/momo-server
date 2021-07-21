//package com.momo.server.service;
//
//import java.math.BigInteger;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestInstance.Lifecycle;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.momo.server.domain.Meet;
//import com.momo.server.domain.User;
//import com.momo.server.dto.request.LoginRequestDto;
//import com.momo.server.repository.UserRepository;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@TestInstance(Lifecycle.PER_CLASS) // @AfterAll 어노테이션에는 필요함
//public class UserServiceTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @AfterEach
//    public void afterEach() {
//	userRepository.deleteUser();// 한번씩 저장소를 지워줌
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;// json매핑을 위해
//
//    @Test
//    public void 유저생성() {
//
//	LoginRequestDto loginRequestDto = new LoginRequestDto();
//
//	User userEntity = new User();
//
//	BigInteger userid = BigInteger.valueOf(Integer.valueOf(Math.abs(loginRequestDto.hashCode())));
//
//	userEntity.setUserId(userid);
//	userEntity.setUsername(loginRequestDto.getUsername());
//	userEntity.setCookieRemember(loginRequestDto.getRemember());
//	userEntity.setMeetId(loginRequestDto.getMeetId());
//
//	Meet meetEntity = meetRepository.findMeet(loginRequestDto.getMeetId());
//
//	int dates = meetEntity.getDates().size();
//	int timeslots = Integer.parseInt(meetEntity.getEnd().split(":")[0])
//		- Integer.parseInt(meetEntity.getStart().split(":")[0]);
//	int[][] userTimes = new int[timeslots * ((int) 60 / meetEntity.getGap())][dates];
//	userEntity.setUserTimes(userTimes);
//
//	meetRepository.addUser(meetEntity, userEntity);
//	userRepository.createUser(userEntity);
//    }
//
//}
