//package com.momo.server.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.momo.server.repository.MeetRepository;
//
//@SpringBootTest
//public class MeetSetupTest {
//
//    @Autowired
//    protected MeetRepository meetRepository;
//
//    @BeforeEach
//    void MeetSetup() {
//	meetRepository.createMeet(null);
//    }
//
//    protected void cleanup() {
//	meetRepository.deleteMeet();
//    }
//
//}

//참조 : https://github.com/depromeet/3dollars-in-my-pocket-backend/blob/2f01c57a7c7fada15aa28043adb84ca6ced6f2f6/threedollar-api/src/test/java/com/depromeet/threedollar/api/service/UserSetUpTest.java