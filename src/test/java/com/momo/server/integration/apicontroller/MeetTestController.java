package com.momo.server.integration.apicontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momo.server.dto.request.MeetSaveRequestDto;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;

public class MeetTestController {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;// json매핑을 위해

    public MeetTestController(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public MvcResult createMeet(MeetSaveRequestDto meetSaveRequestDto) throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                post("/api/meet").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(meetSaveRequestDto))).andDo(print())
            .andExpect(status().isCreated()).andReturn();

        return mvcResult;
        // 참조 https://engkimbs.tistory.com/858
        // http://honeymon.io/tech/2019/10/23/spring-deprecated-media-type.html
    }

    public void createTestMeet() throws Exception {
        ArrayList<LocalDate> testDate = new ArrayList<LocalDate>();

        testDate.add(LocalDate.now().plusDays(5));
        testDate.add(LocalDate.now().plusDays(10));

        MeetSaveRequestDto meetSaveRequestDto = MeetSaveRequestDto.builder().title("테스트용약속생성")
            .start("11:00")
            .end("19:00").dates(testDate).gap(30).video(true).center(true).build();
        createMeet(meetSaveRequestDto);
    }

    public void getMeet(String meetId) throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/meet/" + meetId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.title").value("임시테스트")).andDo(print()).andReturn();

    }

}