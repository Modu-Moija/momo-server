package com.momo.server.setup.apicontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.MeetSaveRequestDto;
import org.springframework.test.web.servlet.MvcResult;

public class MeetTestController {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;// json매핑을 위해

    public MeetTestController(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public MvcResult createMeet(MeetSaveRequestDto meetSaveRequestDto) throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/meet").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(meetSaveRequestDto))).andDo(print())
		.andExpect(status().isCreated()).andReturn();


        return mvcResult;
	// 참조 https://engkimbs.tistory.com/858
	// http://honeymon.io/tech/2019/10/23/spring-deprecated-media-type.html
    }

    public void getMeet(String meetId) throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/meet/" + meetId)) // 1, 2
		.andExpect(status().isOk()) // 4
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.data.title").value("새로운테스트")).andDo(print()).andReturn();

    }

}