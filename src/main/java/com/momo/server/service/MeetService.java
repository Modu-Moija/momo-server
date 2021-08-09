package com.momo.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import com.momo.server.exception.OutOfBound.DatesOutOfBoundsException;
import com.momo.server.exception.valid.InvalidDateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.dto.MeetSubInfo;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.dto.response.MeetInfoRespDto;
import com.momo.server.exception.notfound.MeetNotFoundException;
import com.momo.server.repository.MeetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MeetService {

    private final MeetRepository meetRepository;

    // 약속 생성메소드
    @Transactional
    public ResponseEntity<?> createMeet(MeetSaveRequestDto requestDto, String hashedUrl) {

        Meet meet = new Meet();
        LocalDate startDate = requestDto.getDates().get(0);
        LocalDate endDate = requestDto.getDates().get(1);
        ArrayList<LocalDate> dates = new ArrayList<>();
        LocalDate curDate = startDate;

        fillDatesArray(endDate, dates, curDate);
        validateDates(dates);
        validateStartDate(startDate);

        int[][] temptimes = getRowCol(requestDto, dates);

        // meet로 저장
        setMeet(requestDto, hashedUrl, meet, dates, temptimes);
        meetRepository.createMeet(meet);

        return ResponseEntity.ok().build();
    }

    private void setMeet(MeetSaveRequestDto requestDto, String hashedUrl, Meet meet, ArrayList<LocalDate> dates, int[][] temptimes) {
        meet.setMeetId(hashedUrl);
        meet.setTitle(requestDto.getTitle());
        meet.setStart(requestDto.getStart());
        meet.setEnd(requestDto.getEnd());
        meet.setCreated(LocalDateTime.now().plusHours(9));
        meet.setGap(requestDto.getGap());
        meet.setDates(dates);
        meet.setTimes(temptimes);
        meet.setCenter(requestDto.isCenter());
        meet.setCenter(requestDto.isVideo());
    }

    private int[][] getRowCol(MeetSaveRequestDto requestDto, ArrayList<LocalDate> dates) {

        // meet의 times 이차원 배열 row 계산
        String start = requestDto.getStart().split(":")[0];
        String end = requestDto.getEnd().split(":")[0];
        int row = Integer.parseInt(end) - Integer.parseInt(start);
        row = (int) (60 / requestDto.getGap()) * row;

        // meet의 times 이차원 배열 col 계산
        int col = dates.size();
        return fillTempTimesToZero(row, col);
    }

    private int[][] fillTempTimesToZero(int row, int col) {

        // meet의 times 이차원배열 0으로 채우기
        // 캐시프렌들리코드 https://hot2364928.tistory.com/58
        int[][] temptimes = new int[row][col];
        for (int i = 0; i < row; i++) { // 행 반복
            for (int j = 0; j < col; j++) { // 열 반복
                temptimes[i][j] = 0;
            }
        }
        return temptimes;
    }

    private void fillDatesArray(LocalDate endDate, ArrayList<LocalDate> dates, LocalDate curDate) {
        while (!curDate.equals(endDate.plusDays(1))) {
            dates.add(curDate);
            curDate = curDate.plusDays(1);
        }
    }

    private void validateStartDate(LocalDate startDate) {
        //시작날짜에 대한 예외처리
        if (startDate.compareTo(LocalDate.now()) <0) {
            throw new InvalidDateException();
        }
    }

    private void validateDates(ArrayList<LocalDate> dates) {
        //날짜수 초과에 대한 예외처리
        if (dates != null) {
            if (dates.size() > 31) {
                throw new DatesOutOfBoundsException();
            }
        }
    }

    @Transactional(readOnly = true)
    public MeetInfoRespDto getMeetInfo(String meetId) {
        MeetInfoRespDto meetInfoRespDto = new MeetInfoRespDto();
        Meet meetEntity = meetRepository.findMeet(meetId);
        Optional.ofNullable(meetEntity).orElseThrow(() -> new MeetNotFoundException(meetId));
        setMeetInfoRespDto(meetInfoRespDto, meetEntity);

        return meetInfoRespDto;
    }

    private void setMeetInfoRespDto(MeetInfoRespDto meetInfoRespDto, Meet meetEntity) {
        meetInfoRespDto.setCenter(meetEntity.isCenter());
        meetInfoRespDto.setVideo(meetEntity.isVideo());
        meetInfoRespDto.setTitle(meetEntity.getTitle());
        meetInfoRespDto.setDates(meetEntity.getDates());
        meetInfoRespDto.setStart(meetEntity.getStart());
        meetInfoRespDto.setEnd(meetEntity.getEnd());
        meetInfoRespDto.setGap(meetEntity.getGap());
        meetInfoRespDto.setMeetSubInfo(applyMeetSubInfo(meetEntity));
    }

    // meet정보 반환해줄 때 MeetSub 적용해주는 메소드
    public MeetSubInfo applyMeetSubInfo(Meet meetEntity) {

        MeetSubInfo meetSubInfo = new MeetSubInfo();

        meetSubInfo.setWhen(
                meetEntity.getDates().get(0) + " ~ " + meetEntity.getDates().get(meetEntity.getDates().size() - 1));

        String where = null;
        if (meetEntity.isVideo() == true) {
            where = "화상회의";
        } else {
            where = "대면회의";
        }
        meetSubInfo.setWhere(where);
        meetSubInfo.setWho(meetEntity.getUserNames());

        return meetSubInfo;

    }

}
