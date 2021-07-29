package com.momo.server.integration;

import com.momo.server.BackEndApplicationTests;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.repository.UserRepository;
import com.momo.server.integration.apicontroller.MeetTestController;
import com.momo.server.integration.apicontroller.UserTestController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.TimeSlotRepository;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private UserRepository userRepository;

    private MeetTestController meetTestController;
    private UserTestController userTestController;

    @BeforeAll
    void setUp() throws Exception {
        meetTestController = new MeetTestController(mockMvc, objectMapper);
        meetTestController.createTestMeet();

        userTestController = new UserTestController(mockMvc, objectMapper);
    }

    @AfterAll
    public void afterAll() {
        meetRepository.findAllAndRemoveMeet("48e00ec53bf70f4");
        timeSlotRepository.findAllAndRemoveTimeSlot("48e00ec53bf70f4");
        userRepository.findAndRemoveUser("1817849666");
    }

    @Test
    public void 유저_로그인() throws Exception {

        LoginRequestDto loginRequestDto = LoginRequestDto.builder().remember(true).meetId("48e00ec53bf70f4").username("테스트유저").build();
        userTestController.loginUser(loginRequestDto);
    }
}