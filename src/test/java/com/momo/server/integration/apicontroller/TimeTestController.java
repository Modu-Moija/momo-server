package com.momo.server.integration.apicontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.LoginRequestDto;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TimeTestController {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;// json매핑을 위해
    public MockHttpSession session;

    public TimeTestController(MockMvc mockMvc, ObjectMapper objectMapper, MockHttpSession session) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.session = session;
    }

    public void getUserTime() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/time/usertime").session(session))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.colorDate.8.2").value(60)).andDo(print()).andReturn();
    }

    public void getMostLeast(String meetId) throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/time/" + meetId + "/mostleast"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mostTime[0].num").value(3)).andDo(print()).andReturn();
    }

    public void compareTimtableData() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/time/2d"))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        System.out.println("response body content(B) : " + mvcResult.getResponse().getContentAsByteArray().length);
        MvcResult mvcResult2 = mockMvc.perform(get("/api/time/array"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        System.out.println("response body content(B) : " + mvcResult2.getResponse().getContentAsByteArray().length);
    }
}
