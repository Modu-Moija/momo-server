package com.momo.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.LoginRequestDto;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS) // @AfterAll 어노테이션에는 필요함
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MeetControllerTest meetControllerTest;

    @BeforeAll
    void meetSetu() throws Exception {
	meetControllerTest = new MeetControllerTest(mockMvc, objectMapper);
	meetControllerTest.약속을_생성();
    }

    @Test
    void 유저_로그인() throws Exception {
	// given

	LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("호날두").remember(true)
		.meetId("ea3d15b1adf87f5").build();

	// when & then

	mockMvc.perform(post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequestDto)))
		.andDo(print()).andExpect(status().isOk());

    }

}
