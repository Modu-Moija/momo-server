package com.momo.server.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.integration.apicontroller.TimeTestController;
import com.momo.server.integration.setup.SessionSetup;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TimeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private UserRepository userRepository;

    public MockHttpSession session;

    private TimeTestController timeTestController;

    @BeforeEach
    void setUp() throws Exception {
        BigInteger userId = new BigInteger("760433781");
        session = SessionSetup.sessionSetUp();

        timeTestController = new TimeTestController(mockMvc, objectMapper, session);

    }

    @AfterAll
    public void afterAll() {
    }

//    @Test
//    public void 유저타임업데이트() throws Exception {
//        // given
//        ArrayList<LocalDate> testDate = new ArrayList<LocalDate>();
//
//        testDate.add(LocalDate.now().plusDays(5));
//        testDate.add(LocalDate.now().plusDays(10));
//
//        MeetSaveRequestDto meetSaveRequestDto = MeetSaveRequestDto.builder().title("약속생성테스트").start("11:00")
//                .end("19:00").dates(testDate).gap(30).video(true).center(true).build();
//
//        MvcResult mvcResult = meetTestController.createMeet(meetSaveRequestDto);
//        // 참조 https://engkimbs.tistory.com/858
//        // http://honeymon.io/tech/2019/10/23/spring-deprecated-media-type.html
//    }

    @Test
    public void 유저타임조회() throws Exception {

        timeTestController.getUserTime();
    }

    @Test
    public void 최대최소조회() throws Exception {

        String meetId = "b0f0cb7e67286b8";
        timeTestController.getMostLeast(meetId);
    }
}
