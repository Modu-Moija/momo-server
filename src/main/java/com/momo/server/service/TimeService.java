package com.momo.server.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import com.momo.server.dto.TimeSlotRespEntry;
import com.momo.server.dto.auth.SessionUser;
import com.momo.server.dto.response.MostLeastRespDto;
import com.momo.server.utils.NumConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.exception.notfound.MeetNotFoundException;
import com.momo.server.exception.notfound.UserNotFoundException;
import com.momo.server.repository.MeetRepository;
import com.momo.server.repository.UserRepository;

@Service
public class TimeService {

    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

    @Autowired
    public TimeService(UserRepository userRepository, MeetRepository meetRepository) {
        this.userRepository = userRepository;
        this.meetRepository = meetRepository;
    }

    /*
     * 유저시간 업데이트 및 약속시간 업데이트 메소드
     */
    @Transactional
    public ResponseEntity<?> updateUsertime(SessionUser userEntity,
        UserTimeUpdateRequestDto requestDto) {

        Optional.ofNullable(userEntity)
            .orElseThrow(() -> new UserNotFoundException(userEntity.getUserId()));
        Meet meetEntity = meetRepository.findMeet(userEntity.getMeetId());
        Optional.ofNullable(meetEntity)
            .orElseThrow(() -> new MeetNotFoundException(userEntity.getMeetId()));

        ArrayList<LocalDate> dates = meetEntity.getDates();
        int[][] temp_userTimes = userEntity.getUserTimes();
        int[][] temp_Times = meetEntity.getTimes();
        int gap = meetEntity.getGap();
        String start = meetEntity.getStart();
        ArrayList<String> userNames = meetEntity.getUserNames();
        int num = meetEntity.getNum();
        int userIndex = -1;
        userIndex = getUserIndex(userEntity, userNames, userIndex);
        int totalStartMin = getTotalMin(start);

        int col;
        for (int i = 0; i < dates.size(); i++) {
            // requestDto의 date 찾기
            for (int j = 0; j < requestDto.getUsertimes().size(); j++) {
                if (requestDto.getUsertimes().get(j).getDate().equals(dates.get(i))) {
                    col = i;
                    // requestDto의 시간배열 크기만큼 반복
                    for (int t = 0; t < requestDto.getUsertimes().get(j).getTimeslots().size();
                        t++) {
                        String timeslot = requestDto.getUsertimes().get(j).getTimeslots().get(t)
                            .getTime();
                        int row = getRowByUserTimeUpdate(gap, totalStartMin, timeslot);
                        // true일 때 좌표값 1로 세팅,false일때 좌표값 0으로 세팅
                        if (requestDto.getUsertimes().get(j).getTimeslots().get(t).getPossible()) {
                            setOneToUserMeetTime(temp_userTimes, temp_Times[row], num, userIndex,
                                col, row);
                        } else if (!requestDto.getUsertimes().get(j).getTimeslots().get(t)
                            .getPossible()) {
                            setZeroToUserMeetTime(temp_userTimes, temp_Times[row], num, userIndex,
                                col, row);
                        }
                    }
                }
            }
        }
        userRepository.updateUserTime(userEntity, temp_userTimes);
        meetRepository.updateMeetTime(userEntity.getMeetId(), temp_Times);
        return ResponseEntity.ok().build();
    }

    private int getUserIndex(SessionUser userEntity, ArrayList<String> userNames, int userIndex) {
        if (userNames != null) {
            userIndex = userNames.indexOf(userEntity.getUsername());
        }
        return userIndex;
    }

    private void setZeroToUserMeetTime(int[][] temp_userTimes, int[] temp_time, int num,
        int userIndex, int col, int row) {
        temp_userTimes[row][col] = 0;
        int[] bin = NumConvert.decToBin(num, temp_time[col]);
        bin[userIndex] = 0;
        int dec = NumConvert.binToDec(num, bin);
        temp_time[col] = dec;
    }

    private void setOneToUserMeetTime(int[][] temp_userTimes, int[] temp_time, int num,
        int userIndex, int col, int row) {
        temp_userTimes[row][col] = 1;
        // 1. 기존의 10진수를 2진수로 변환
        int[] bin = NumConvert.decToBin(num, temp_time[col]);
        // 2.userindex의 위치 1로 변경
        bin[userIndex] = 1;
        // 3. 다시 2진수를 10진수로 변환해서 저장
        int dec = NumConvert.binToDec(num, bin);
        temp_time[col] = dec;
    }

    /*
    row 위치 계산
     */
    private int getRowByUserTimeUpdate(int gap, int totalStartMin, String timeslot) {
        int row;
        int input_total_hour = getTotalMin(timeslot);
        row = (input_total_hour - totalStartMin) / gap;
        return row;
    }

    /*
    시간을 분으로 만드는 메소드(11:30을 690으로 바꾸는 메소드)
     */
    private int getTotalMin(String start) {
        int hour = Integer.parseInt(start.substring(0, 2));
        int min = Integer.parseInt(start.substring(3, 5));
        return hour * 60 + min;
    }

    /*
     * 약속관련정보 매핑하는 메소드
     */
    @Transactional(readOnly = true)
    public UserMeetRespDto mapUserMeetRespDto(SessionUser user) {

        UserMeetRespDto userMeetRespDto = new UserMeetRespDto();
        LinkedHashMap<String, LinkedHashMap<String, Boolean>> planList = new LinkedHashMap<>();

        User userEntity = userRepository.findUser(user);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(user.getUserId()));
        Meet meetEntity = meetRepository.findMeet(user.getMeetId());
        Optional.ofNullable(meetEntity)
            .orElseThrow(() -> new MeetNotFoundException(user.getMeetId()));

        int[][] userTimes = userEntity.getUserTimes();
        int gap = meetEntity.getGap();
        LocalDate startDate = meetEntity.getDates().get(0);
        String start = meetEntity.getStart();
        Integer dayOfMonth = startDate.getDayOfMonth();
        int totalStartMin = getTotalMin(start);

        // 2차원 배열 돌면서 데이터 매핑
        for (int i = 0; i < userTimes[0].length; i++) {
            LinkedHashMap<String, Boolean> timeMap = new LinkedHashMap<>();
            String temp_date =
                startDate.getYear() + "-" + addZeroToValue(startDate.getMonthValue()) + "-" + addZeroToValue(dayOfMonth);
            int temp_totalStartMin = totalStartMin;
            for (int j = 0; j < userTimes.length; j++) {
                mapToTimeMap(userTimes, i, timeMap, temp_totalStartMin, j);
                temp_totalStartMin = temp_totalStartMin + gap;
            }
            dayOfMonth++;
            planList.put(temp_date, timeMap);
        }
        setUserMeetRespDto(user, userMeetRespDto, planList, meetEntity);
        return userMeetRespDto;
    }

    private String addZeroToValue(int num){
        String strNum = String.valueOf(num);
        if(strNum.length()==1){
            return "0"+strNum;
        }else{
            return strNum;
        }
    }

    private void mapToTimeMap(int[][] userTimes, int i, LinkedHashMap<String, Boolean> timeMap,
        int temp_totalStartTime, int j) {
        String key = String.valueOf(temp_totalStartTime / 60) + ":" + String.valueOf(
            addZeroToValue(temp_totalStartTime % 60));
        if (userTimes[j][i] == 0) {
            timeMap.put(key, false);
        } else if (userTimes[j][i] == 1) {
            timeMap.put(key, true);
        }
    }

    private void setUserMeetRespDto(SessionUser user, UserMeetRespDto userMeetRespDto,
        LinkedHashMap<String, LinkedHashMap<String, Boolean>> planList, Meet meetEntity) {
        userMeetRespDto.setMeetId(user.getMeetId());
        userMeetRespDto.setPlanList(planList);
        userMeetRespDto.setColorDate(getColorDate(meetEntity));
    }

    /*
    각 날짜별 되는 사람 구하는 연산
     */
    @Transactional(readOnly = true)
    public LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> getColorDate(Meet meetEntity) {
        LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> monthDayMap = new LinkedHashMap<>();

        ArrayList<Integer> colorDate = new ArrayList<>();
        int[][] times = meetEntity.getTimes();
        ArrayList<LocalDate> dates = meetEntity.getDates();

        sumTwoDimArrayVertical(colorDate, times);
        Month firstMonth = dates.get(0).getMonth();
        //전체 날짜만큼 반복
        for (int i = 0; i < dates.size(); ) {
            //달의 날짜 개수 세기 ex) 7월 26,27,28,29,30,31일이면 6
            int MonthDayCount = 0;
            for (LocalDate date : dates) {
                if (date.getMonth() == firstMonth) {
                    MonthDayCount++;
                }
            }
            LinkedHashMap<Integer, Integer> dayCountMap = new LinkedHashMap<>();
            //해당 달의 날짜수만큼 반복하면서 날짜와 colorDate 맵에 입력
            for (int j = 0; j < MonthDayCount; j++, i++) {
                dayCountMap.put(dates.get(i).getDayOfMonth(), colorDate.get(i));
            }
            //해당 달의 값 입력
            monthDayMap.put(firstMonth.getValue(), dayCountMap);
            firstMonth = firstMonth.plus(1);
        }
        return monthDayMap;
    }

    private void sumTwoDimArrayVertical(ArrayList<Integer> colorDate, int[][] times) {
        int temp = 0;
        for (int j = 0; j < times[0].length; j++) {
            for (int[] time : times) {
                temp = temp + time[j];
            }
            colorDate.add(j, temp);
            temp = 0;
        }
    }

    @Transactional(readOnly = true)
    public MostLeastRespDto getMostLeastTime(String meetId) {
        MostLeastRespDto mostLeastRespDto = new MostLeastRespDto();
        Meet meetEntity = meetRepository.findMeet(meetId);
        List<TimeSlotRespEntry> mostTimeSlots = this.mapToTimeSlotEntry(meetEntity);
        List<TimeSlotRespEntry> leasttimeSlots = this.mapToTimeSlotEntry(meetEntity);

        sortByMostTime(mostTimeSlots);
        mostLeastRespDto.setMostTime(mostTimeSlots);

        sortByLeastTime(leasttimeSlots);
        mostLeastRespDto.setLeastTime(leasttimeSlots);
        return mostLeastRespDto;
    }

    private void sortByLeastTime(List<TimeSlotRespEntry> leasttimeSlots) {
        leasttimeSlots.sort((t1, t2) -> {
            //숫자 같으면, 날짜, 시간 빠른순으로 반환
            int res = t1.getNum().compareTo(t2.getNum());
            if (res == 0) {
                res = t1.getDate().compareTo(t2.getDate());
            } else if (res == 0) {
                res = t1.getTime().compareTo(t2.getTime());
            }
            return res;
        });
    }

    private void sortByMostTime(List<TimeSlotRespEntry> mostTimeSlots) {
        mostTimeSlots.sort((t1, t2) -> {
            //숫자 같으면, 날짜, 시간 빠른순으로 반환
            int res = t2.getNum().compareTo(t1.getNum());
            if (res == 0) {
                res = t1.getDate().compareTo(t2.getDate());
            } else if (res == 0) {
                res = t1.getTime().compareTo(t2.getTime());
            }
            return res;
        });
    }

    /*
    meet의 이차원 배열을 timeslot으로 매핑시키는 메소드(BFS 알고리즘 활용으로 개선 가능)
     */
    public List<TimeSlotRespEntry> mapToTimeSlotEntry(Meet meetEntity) {

        List<TimeSlotRespEntry> list = new ArrayList<>();
        ArrayList<LocalDate> dates = meetEntity.getDates();
        ArrayList<String> users = meetEntity.getUserNames();
        int[][] times = meetEntity.getTimes();
        int num = meetEntity.getNum();
        int gap = meetEntity.getGap();
        String start = meetEntity.getStart();
        int totalStartMin = getTotalMin(start);

        for (int i = 0; i < times[0].length; i++) {
            int tempStartMin = totalStartMin;
            for (int j = 0; j < times.length; j++, tempStartMin = tempStartMin + gap) {
                TimeSlotRespEntry timeSlotRespEntry = new TimeSlotRespEntry();
                ArrayList<String> timeSlotUsers = new ArrayList<>();
                int possibleMinStart = tempStartMin;
                String possibleStart = splitToHourMin(possibleMinStart);
                //숫자가 같을때까지 돌리기
                while (j < times.length - 1) {
                    if (times[j][i] != times[j + 1][i]) {
                        break;
                    }
                    j = j + 1;
                    tempStartMin = tempStartMin + gap;
                }
                int possibleMinEnd = tempStartMin + gap;
                String possibleEnd = splitToHourMin(possibleMinEnd);

                int[] bin = NumConvert.decToBin(num, times[j][i]);
                addTimeSlotUsers(users, timeSlotUsers, bin);
                setTimeSlotRespEntry(meetEntity, dates, i, timeSlotRespEntry, timeSlotUsers,
                    possibleStart, possibleEnd);
                list.add(timeSlotRespEntry);
            }
        }
        return list;
    }

    /*
    분을 시간과 분으로 나눠주는 메소드 ex)330 -> 5:30
     */
    private String splitToHourMin(int totalMin) {
        String tempRestMin;
        int restStartMin = totalMin % 60;
        tempRestMin = getStringMinFromZero(restStartMin);
        return String.valueOf(totalMin / 60) + ":" + tempRestMin;
    }

    private void addTimeSlotUsers(ArrayList<String> users, ArrayList<String> timeSlotUsers,
        int[] bin) {
        for (int t = 0; t < bin.length; t++) {
            if (bin[t] == 1) {
                timeSlotUsers.add(users.get(t));
            }
        }
    }

    private void setTimeSlotRespEntry(Meet meetEntity, ArrayList<LocalDate> dates, int i,
        TimeSlotRespEntry timeSlotRespEntry, ArrayList<String> timeSlotUsers, String possibleStart,
        String possibleEnd) {
        timeSlotRespEntry.setDate(dates.get(i));
        timeSlotRespEntry.setMeetId(meetEntity.getMeetId());
        timeSlotRespEntry.setUsers(timeSlotUsers);
        timeSlotRespEntry.setTime(possibleStart + " ~ " + possibleEnd);
        timeSlotRespEntry.setNum(timeSlotUsers.size());
    }

    /*
    분이 0일 때 00으로 반환해주기 위한 메소드
     */
    private String getStringMinFromZero(int restMin) {
        String strRestMin = null;
        if (restMin == 0) {
            strRestMin = String.valueOf(restMin) + "0";
        } else {
            strRestMin = String.valueOf(restMin);
        }
        return strRestMin;
    }

}
