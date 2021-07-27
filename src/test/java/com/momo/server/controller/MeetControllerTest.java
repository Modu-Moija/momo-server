package com.momo.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.TimeSlotRepository;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS) // @AfterAll 어노테이션에는 필요함
public class MeetControllerTest {

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public MeetControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {

	this.mockMvc = mockMvc;
	this.objectMapper = objectMapper;
    }

    @AfterAll
    public void afterAll() {// 생성된 약속 찾아서 지워주기 @Transactional이 동작안해서직접 구현함
	meetRepository.findAllAndRemoveMeet("e8e1bf2ec5ab131");
	timeSlotRepository.findAllAndRemoveTimeSlot("e8e1bf2ec5ab131");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;// json매핑을 위해

    @Test
    public void 약속을_생성() throws Exception {

	// given
	ArrayList<LocalDate> testDate = new ArrayList<LocalDate>();

	testDate.add(LocalDate.parse("2021-07-20"));
	testDate.add(LocalDate.parse("2021-07-30"));

	MeetSaveRequestDto meetSaveRequestDto = MeetSaveRequestDto.builder().title("약속생성테스트").start("11:00")
		.end("19:00").dates(testDate).gap(30).video(true).center(true).build();

	// when & then

	// System.out.println(meetSaveRequestDto);
	mockMvc.perform(post("/api/meet").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(meetSaveRequestDto))).andDo(print())
		.andExpect(status().isCreated());

	// 참조 https://engkimbs.tistory.com/858
	// http://honeymon.io/tech/2019/10/23/spring-deprecated-media-type.html
    }

    @Test
    public void 약속조회() throws Exception {

	String meetId = "e8e1bf2ec5ab131";

	mockMvc.perform(get("/api/meet/" + meetId)) // 1, 2
		.andExpect(status().isOk()) // 4
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.data.title").value("약속생성테스트")).andDo(print());

    }

}
