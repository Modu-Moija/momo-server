package com.momo.server.setup.apicontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.dto.request.MeetSaveRequestDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserTestController {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;// json매핑을 위해

    public UserTestController(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public void loginUser(LoginRequestDto loginRequestDto) throws Exception {

        mockMvc.perform(post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(loginRequestDto))).andDo(print())
		.andExpect(status().isOk()).andReturn();
    }

}