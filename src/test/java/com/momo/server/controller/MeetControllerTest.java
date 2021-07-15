package com.momo.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.MeetSaveRequestDto;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(MeetController.class)
public class MeetControllerTest {
	
	@Autowired
	  private MockMvc mockMvc;
	
	@Autowired
	  private ObjectMapper objectMapper;//json매핑을 위해

	@Test
	public void 약속을_생성() throws Exception {
		
//		
//		//given
//		String content = objectMapper.writeValueAsString(new MeetSaveRequestDto(null, "약속생성테스트", "11:00", "19:00",null,  ["2021-07-20", "2021-07-30"], true, true, 30);
//		
//		
//		new MeetSaveRequestDto(content, content, content, content, null, 0, null, 0, null, null, false, false)
//		//when & then
//
//		mockMvc.perform(post("/api/meet")
//		        .content(content)
//		        .contentType(MediaType.APPLICATION_JSON)
//		        .accept(MediaType.APPLICATION_JSON))
//		        .andExpect(status().isOk())
//		        .andExpect(content().string("데일의 블로그입니다. dale"))
//		        .andDo(print());
//		
	}
}
