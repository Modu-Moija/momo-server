package com.momo.server.service;

import java.time.LocalDate;
import java.util.*;

import com.momo.server.dto.TimeSlotRespEntry;
import com.momo.server.dto.auth.SessionUser;
import com.momo.server.dto.response.MostLeastRespDto;
import com.momo.server.utils.NumConvert;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final UserRepository userRepository;
    private final MeetRepository meetRepository;

    /*
     * 유저시간 업데이트 및 약속시간 업데이트 메소드
     */
    @Transactional
    public ResponseEntity<?> updateUsertime(SessionUser userEntity, UserTimeUpdateRequestDto requestDto) {

        Optional.ofNullable(userEntity).orElseThrow(() -> new UserNotFoundException(userEntity.getUserId()));
        Meet meetEntity = meetRepository.findMeet(userEntity.getMeetId());
        Optional.ofNullable(meetEntity).orElseThrow(() -> new MeetNotFoundException(userEntity.getMeetId()));

        // dbmeet로 column 위치 계산
        ArrayList<LocalDate> dates = meetEntity.getDates();
        int[][] temp_userTimes = userEntity.getUserTimes();
        int[][] temp_Times = meetEntity.getTimes();
        int gap = meetEntity.getGap();
        String start = meetEntity.getStart();
        ArrayList<String> userNames = meetEntity.getUserNames();
        int num = meetEntity.getNum();
        int userIndex = -1;
        if (userNames != null) {
            userIndex = userNames.indexOf(userEntity.getUsername());
        }
        int totalStartMin = getTotalMin(start);

        int col = 0;
        // db meet의 date로 날짜 찾기
        for (int i = 0; i < dates.size(); i++) {
            // requestDto의 date 찾기
            for (int j = 0; j < requestDto.getUsertimes().size(); j++) {
                if (requestDto.getUsertimes().get(j).getDate().equals(dates.get(i))) {
                    col = i;
                    // requestDto의 시간배열 크기만큼 반복
                    for (int t = 0; t < requestDto.getUsertimes().get(j).getTimeslots().size(); t++) {
                        String timeslot = requestDto.getUsertimes().get(j).getTimeslots().get(t).getTime();

                        // dbmeet로 row 위치 계산
                        int row = 0;
                        int input_hour = Integer.parseInt(timeslot.substring(0, 2));
                        int input_min = Integer.parseInt(timeslot.substring(3, 5));
                        int input_total_hour = input_hour * 60 + input_min;
                        row = (input_total_hour - totalStartMin) / gap;

                        // true일 때 좌표값 1로 세팅,false일때 좌표값 0으로 세팅
                        if (requestDto.getUsertimes().get(j).getTimeslots().get(t).getPossible() == true) {
                            temp_userTimes[row][col] = 1;
                            // 1. 기존의 10진수를 2진수로 변환
                            int[] bin = NumConvert.decToBin(num, temp_Times[row][col]);
                            // 2.userindex의 위치 1로 변경
                            bin[userIndex] = 1;
                            // 3. 다시 2진수를 10진수로 변환해서 저장
                            int dec = NumConvert.binToDec(num, bin);
                            temp_Times[row][col] = dec;

                        } else if (requestDto.getUsertimes().get(j).getTimeslots().get(t).getPossible() == false) {
                            temp_userTimes[row][col] = 0;
                            int[] bin = NumConvert.decToBin(num, temp_Times[row][col]);
                            bin[userIndex] = 0;
                            int dec = NumConvert.binToDec(num, bin);
                            temp_Times[row][col] = dec;
                        }
                    }
                }
            }
        }
        userRepository.updateUserTime(userEntity, temp_userTimes);

        // Meet 시간 업데이트
        meetRepository.updateMeetTime(userEntity.getMeetId(), temp_Times);
        return ResponseEntity.ok().build();
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
        LinkedHashMap<String, LinkedHashMap<String, Boolean>> planList = new LinkedHashMap<String, LinkedHashMap<String, Boolean>>();

        // 데이터 db에서 불러오기, 세션에서 가져오면 안될듯
        User userEntity = userRepository.findUser(user);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(user.getUserId()));

        Meet meetEntity = meetRepository.findMeet(user.getMeetId());
        Optional.ofNullable(meetEntity).orElseThrow(() -> new MeetNotFoundException(user.getMeetId()));

        int[][] userTimes = userEntity.getUserTimes();
        String start = meetEntity.getStart();
        String end = meetEntity.getEnd();
        int gap = meetEntity.getGap();
        LocalDate startDate = meetEntity.getDates().get(0);
        int dayOfMonth = startDate.getDayOfMonth();

        int hour = Integer.parseInt(start.split(":")[0]);
        int gapTime = Integer.parseInt(start.split(":")[1]);
        int totalStartTime = hour * 60 + gapTime;

        // 2차원 배열 돌면서 데이터 저장
        for (int i = 0; i < userTimes[0].length; i++) {

            // 순서 저장을 위해 링크드해쉬맵 사용
            LinkedHashMap<String, Boolean> time = new LinkedHashMap<String, Boolean>();

            String temp_date = String.valueOf(startDate.getYear()) + "/" + String.valueOf(startDate.getMonthValue())
                    + "/" + String.valueOf(dayOfMonth);
            int temp_totalStartTime = totalStartTime;

            for (int j = 0; j < userTimes.length; j++) {
                String key = String.valueOf(temp_totalStartTime / 60) + ":" + String.valueOf(temp_totalStartTime % 60);
                if (userTimes[j][i] == 0) {
                    time.put(key, false);
                } else if (userTimes[j][i] == 1) {
                    time.put(key, true);
                }
                temp_totalStartTime = temp_totalStartTime + gap;
            }
            dayOfMonth++;
            planList.put(temp_date, time);
        }

        userMeetRespDto.setMeetId(user.getMeetId());
        userMeetRespDto.setPlanList(planList);
        userMeetRespDto.setColorDate(getColorDate(meetEntity));

        return userMeetRespDto;
    }

    // 날짜 색깔 구하는 연산
    @Transactional(readOnly = true)
    public ArrayList<Integer> getColorDate(Meet meetEntity) {
        ArrayList<Integer> colorDate = new ArrayList<Integer>();

        int[][] times = meetEntity.getTimes();

        int temp = 0;
        for (int j = 0; j < times[0].length; j++) {
            for (int i = 0; i < times.length; i++) {
                temp = temp + times[i][j];
            }
            colorDate.add(j, temp);
            temp = 0;
        }
        return colorDate;
    }

    @Transactional(readOnly = true)
    public MostLeastRespDto getMostLeastTime(String meetId) {
        MostLeastRespDto mostLeastRespDto = new MostLeastRespDto();
        Meet meetEntity = meetRepository.findMeet(meetId);
        List<TimeSlotRespEntry> mostTimeSlots = this.mapToTimeSlotEntry(meetEntity);
        //least를 위해 mosttimeSlots 배열 복사
        List<TimeSlotRespEntry> leasttimeSlots = this.mapToTimeSlotEntry(meetEntity);

        Collections.sort(mostTimeSlots, new Comparator<TimeSlotRespEntry>() {

            @Override
            public int compare(TimeSlotRespEntry t1, TimeSlotRespEntry t2) {
                //숫자 같으면, 날짜, 시간 빠른순으로 반환
                int res = t2.getNum().compareTo(t1.getNum());
                if (res == 0) {
                    res = t1.getDate().compareTo(t2.getDate());
                } else if (res == 0) {
                    res = t1.getTime().compareTo(t2.getTime());
                }
                // num순 정렬
                return res;
            }

        });
        mostLeastRespDto.setMostTime(mostTimeSlots);

        Collections.sort(leasttimeSlots, new Comparator<TimeSlotRespEntry>() {

            @Override
            public int compare(TimeSlotRespEntry t1, TimeSlotRespEntry t2) {
                //숫자 같으면, 날짜, 시간 빠른순으로 반환
                int res = t1.getNum().compareTo(t2.getNum());
                if (res == 0) {
                    res = t1.getDate().compareTo(t2.getDate());
                } else if (res == 0) {
                    res = t1.getTime().compareTo(t2.getTime());
                }
                // num순 정렬
                return res;
            }

        });

        mostLeastRespDto.setLeastTime(leasttimeSlots);
        return mostLeastRespDto;
    }

    /*
    meet의 이차원 배열을 timeslot으로 매핑시키는 메소드(BFS 알고리즘 활용으로 개선 가능)
     */
    public List<TimeSlotRespEntry> mapToTimeSlotEntry(Meet meetEntity) {

        List<TimeSlotRespEntry> list = new ArrayList<TimeSlotRespEntry>();
        NumConvert numConvert = new NumConvert();
        ArrayList<LocalDate> dates = meetEntity.getDates();
        ArrayList<String> users = meetEntity.getUserNames();
        int[][] times = meetEntity.getTimes();
        int num = meetEntity.getNum();
        int gap = meetEntity.getGap();
        String start = meetEntity.getStart();
        int totalStartMin = getTotalMin(start);
        for (int i = 0; i < times[0].length; i++) {
            int tempStartMin = totalStartMin;
            for (int j = 0; j < times.length;j++,tempStartMin = tempStartMin + gap) {
                TimeSlotRespEntry timeSlotRespEntry = new TimeSlotRespEntry();
                ArrayList<String> timeSlotUsers = new ArrayList<>();
                timeSlotRespEntry.setDate(dates.get(i));
                timeSlotRespEntry.setMeetId(meetEntity.getMeetId());
                int possibleMinStart = tempStartMin;
                int restStartMin = possibleMinStart % 60;
                String strRestMin = null;
                strRestMin = getStringMinFromZero(restStartMin);
                String possibleStart = String.valueOf(possibleMinStart / 60) + ":" + strRestMin;
                //숫자가 같을때까지 돌리기
                while(j < times.length - 1){
                    if(times[j][i] != times[j + 1][i]){
                        break;
                    }
                    j=j+1;
                    tempStartMin = tempStartMin + gap;
                }
                int possibleMinEnd = tempStartMin + gap;
                int restEndMin = possibleMinEnd % 60;
                strRestMin = getStringMinFromZero(restEndMin);
                String possibleEnd = String.valueOf(possibleMinEnd / 60) + ":" + strRestMin;
                int[] bin = numConvert.decToBin(num, times[j][i]);
                for (int t = 0; t < bin.length; t++) {
                    if (bin[t] == 1) {
                        timeSlotUsers.add(users.get(t));
                    }
                }
                timeSlotRespEntry.setUsers(timeSlotUsers);
                timeSlotRespEntry.setTime(possibleStart + " ~ " + possibleEnd);
                timeSlotRespEntry.setNum(timeSlotUsers.size());
                list.add(timeSlotRespEntry);
            }

        }
        return list;
    }

    /*
    분이 0일 때 00으로 반환해주기 위한 메소드
     */
    private String getStringMinFromZero(int restMin) {
        String strRestMin = null;
        if(restMin ==0){
            strRestMin =String.valueOf(restMin)+"0";
        } else{
            strRestMin =String.valueOf(restMin);
        }
        return strRestMin;
    }

}
