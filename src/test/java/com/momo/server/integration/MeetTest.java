package com.momo.server.integration;


import java.time.LocalDate;
import java.util.ArrayList;

import com.momo.server.integration.apicontroller.MeetTestController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.repository.MeetRepository;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class MeetTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetRepository meetRepository;

    private MeetTestController meetTestController;

//    @BeforeEach
//    void setUp() throws Exception {
//        meetTestController = new MeetTestController(mockMvc, objectMapper);
//    }
//
//    @AfterAll
//    public void afterAll() {// 생성된 약속 찾아서 지워주기 @Transactional이 동작안해서직접 구현함
//        meetRepository.removeMeetByTitle("약속생성테스트");
//    }
//
//    @Test
//    public void 약속생성() throws Exception {
//        // given
//        ArrayList<LocalDate> testDate = new ArrayList<LocalDate>();
//
//        testDate.add(LocalDate.now().plusDays(5));
//        testDate.add(LocalDate.now().plusDays(10));
//
//        MeetSaveRequestDto meetSaveRequestDto = MeetSaveRequestDto.builder().title("약속생성테스트")
//            .start("11:00")
//            .end("19:00").dates(testDate).gap(30).video(true).center(true).build();
//
//        MvcResult mvcResult = meetTestController.createMeet(meetSaveRequestDto);
//        // 참조 https://engkimbs.tistory.com/858
//        // http://honeymon.io/tech/2019/10/23/spring-deprecated-media-type.html
//    }
//
//    @Test
//    public void 약속조회() throws Exception {
//
//        String meetId = "7c0570c5542f5c7";
//        meetTestController.getMeet(meetId);
//    }

}