package com.momo.server.controller;

import com.momo.server.dto.MyTimeDto;
import com.momo.server.dto.PositionDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.annotation.Resource;

import com.momo.server.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;

@RestController
@RequestMapping("/api/time")
public class TimeController {

    @Resource
    private UserInfoDto userInfo;

    @Autowired
    private MongoTemplate mongoTemplate;


    @GetMapping("/topN")
    public String[] topN() {
        //top3 시간대 리턴하는 메소드. meet의 checkArray를 가져와서 연산.

        User user = userInfo.getUser();

        if (user == null) {
            return null;
        }

        Meet meet = User.getMeet(mongoTemplate, user.getMeetId());
        int[] total = meet.getCheckArray();
        int notation = meet.getNum() + 1;
        int row = meet.getDates().size();
        int col = total.length;
        int[][] table = new int[col][];
        int[][] visited = new int[col][row];

        Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();

        String[] top3 = new String[3];

        for (int i = 0; i < col; i++) {

            table[i] = transferToN(total[i], notation, row);

        }

        for (int i = 0; i < row; i++) {

            for (int j = 0; j < col; j++) {

                if (visited[j][i] == 0 && table[j][i] > 0) {

                    int count = table[j][i];

                    String positions = BFS(j, i, table, visited);

                    if (!map.containsKey(count)) {

                        map.put(count, new ArrayList<String>());
                        map.get(count).add(positions);

                    } else {
                        map.get(count).add(positions);
                    }
                }
            }
        }

        Iterator it = map.keySet().iterator();

        while (it.hasNext()) {

            ArrayList<String> origin = map.get(it.next());
            Collections.sort(origin, new AscendingString());

        }

        TreeMap<Integer, ArrayList<String>> treeMap = new TreeMap<>(map);
        it = treeMap.descendingKeySet().iterator();
        int index = 0;

        while (index < 3 && it.hasNext()) {

            ArrayList<String> positions = map.get(it.next());

            for (String str : positions) {

                top3[index++] = str;

                if (index == 3) {
                    break;
                }

            }

        }

        index = 0;

        for (String top : top3) {

            if (top == null) {
                continue;
            }

            String[] times = top.split("\\|");
            String[] startTop = times[0].split(",");
            String[] endTop;

            if (times.length > 1) {

                endTop = times[times.length - 1].split(",");

            } else {
                endTop = startTop;
            }

            LocalDate day = meet.getDates().get(Integer.parseInt(startTop[1]));
            String result = day.format(DateTimeFormatter.ofPattern("MM/dd (E)")).toString() + "\n";

            int gap = meet.getGap();

            String[] originStart = meet.getStart().split(":");

            int originHH = Integer.parseInt(originStart[0]);
            int originMM = Integer.parseInt(originStart[1]);

            LocalTime origin = LocalTime.of(originHH, originMM);

            int from = Integer.parseInt(startTop[0]);
            int rear = Integer.parseInt(endTop[0]);

            int offset = gap * from;

            LocalTime start = origin.plusMinutes(gap * from);
            LocalTime end = start.plusMinutes((rear - from + 1) * gap);

            DateTimeFormatter df = DateTimeFormatter.ofPattern("hh:mm a");
            result += start.format(df).toString();

            if (times.length > 1) {
                result += " ~ " + end.format(df).toString();
            }

            top3[index++] = result;

        }

        return top3;
    }

    class AscendingString implements Comparator<String> {

        @Override
        public int compare(String a, String b) {

            return b.split("|").length - a.split("|").length;

        }
    }


    public String BFS(int x, int y, int[][] table, int[][] visited) {

        int xx = x;
        int yy = y;

        int value = table[x][y];

        String result = PositionDto.pos2str(xx++, yy);

        while (xx < table.length && table[xx][yy] == value) {

            result += "|" + PositionDto.pos2str(xx, yy);
            visited[xx++][yy] = 1;

        }

        return result;

    }

    @PutMapping
    public ResponseEntity<?> myTime(@RequestBody MyTimeDto myTime) {

        User user = userInfo.getUser();

        if (user == null) {
            user = User.getUser(mongoTemplate, myTime.getUserId(), myTime.getMeetId());
        }

        Meet meet = User.getMeet(mongoTemplate, user.getMeetId());

        int col = myTime.getCheckArray().length;

        if (meet.getCheckArray().length != col) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        int row = meet.getDates().size();
        int[] checkArray = myTime.getCheckArray();
        int[][] times = new int[col][row];

        for (int i = 0; i < col; i++) {

            int value = checkArray[i];
            String stringBinary = String.format("%0" + row + "d",
                Integer.parseInt(Integer.toBinaryString(value).toString()));

            for (int j = 0; j < row; j++) {

                times[i][j] = stringBinary.charAt(j) - '0';

            }

        }

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getUserKey()));
        User queryUser = mongoTemplate.findOne(query, User.class);

        Update update = new Update();
        update.set("userTimes", times);

        HttpStatus status;

        if (userTimeChanged(queryUser.getUserTimes(), times)) {

            mongoTemplate.updateFirst(query, update, "user");
            status = HttpStatus.CREATED;

        } else {
            status = HttpStatus.OK;
        }

        meet = updateTotalTable(meet, user);
        meet.setUserTime(times);

        return new ResponseEntity<Meet>(meet, status);

    }

    @GetMapping
    @ResponseBody
    public Map possibleTime() {

        Map possibleTimeInfo = new HashMap<String, Object>();
        Meet meet = userInfo.getUser().getMeet(mongoTemplate, userInfo.getUser().getMeetId());

        String start = meet.getStart().split(":")[0];
        String end = meet.getEnd().split(":")[0];
        int startTime = Integer.parseInt(start);
        int endTime = Integer.parseInt(end);

        possibleTimeInfo.put("startTime", startTime);
        possibleTimeInfo.put("endTime", endTime);

        LocalDate startDate = meet.getDates().get(0);
        int length = meet.getDates().size();
        LocalDate endDate = meet.getDates().get(length - 1);

        possibleTimeInfo.put("startDate", startDate);
        possibleTimeInfo.put("endDate", endDate);

        ArrayList<LocalDate> dates = meet.getDates();
        List<String> days = new ArrayList<String>();

        for (LocalDate date : dates) {
            days.add(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN));
        }

        possibleTimeInfo.put("days", days);

        return possibleTimeInfo;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMyTime(MyTimeDto time) {

        User user = userInfo.getUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Meet myMeet = User.getMeet(mongoTemplate, user.getMeetId());

        int col = myMeet.getCheckArray().length;
        int row = myMeet.getDates().size();

        int[][] reset = new int[col][row];

        user.setUserTimes(reset);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getUserKey()));
        mongoTemplate.findAndReplace(query, user);

        updateTotalTable(myMeet, user);

        return ResponseEntity.ok().build();

    }


    public boolean userTimeChanged(int[][] before, int[][] after) {

        int col = before.length;
        int row = before[0].length;

        for (int i = 0; i < col; i++) {

            for (int j = 0; j < row; j++) {

                if (before[i][j] != after[i][j]) {
                    return true;
                }
            }
        }

        return false;
    }

    public Meet updateTotalTable(Meet meet, User client) {
        //한 약속에 참여한 사용자들의 공동 시간표를 업데이트 하는 메소드.

        ArrayList<User> users = new ArrayList<User>();
        int col = Integer.parseInt(meet.getEnd().split(":")[0]) - Integer
            .parseInt(meet.getStart().split(":")[0]);
        col = (int) (60 / meet.getGap()) * col;
        int row = meet.getDates().size();
        int[] totalTable = new int[col];
        int num = meet.getNum();
        int notation = num + 1;
        int[][] checkUsers = new int[col][row];

        //checkArray : 단순히 해당 시간대에 몇 명이 가능한지 표현함. 1차원 배열
        //1. 사용자들의 timetable 정보를 불러온다.
        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(meet.getMeetId()));
        users = (ArrayList<User>) mongoTemplate.find(query, User.class); //순서 중요.

        //2. 2차원 배열을 돌면서 계산한다.

        for (int i = 0; i < col; i++) {

            int[] value = transferToN(totalTable[i], notation, row);

            for (int j = 0; j < row; j++) {

                String check = new String();

                for (User user : users) {

                    int[][] userTime = user.getUserTimes();
                    int timeValue = userTime[i][j];

                    if (timeValue != 0) {

                        value[j] += 1;
                        check += '1';

                    } else {

                        check += '0';

                    }

                }

                int checkUser = Integer.parseInt(check, 2);
                checkUsers[i][j] = checkUser;

            }

            //한줄 계산 다 끝남.

            int updated = 0;

            for (int j = 0; j < row; j++) {

                updated += Math.pow(notation, row - j - 1) * (value[j]);

            }

            totalTable[i] = updated;

        }

        Update update = new Update();
        update.set("checkArray", totalTable);
        update.set("checkUser", checkUsers);
        FindAndModifyOptions option = new FindAndModifyOptions();
        option.returnNew(true);

        return (Meet) mongoTemplate.findAndModify(query, update, option, Meet.class, "meet");

    }

    public String test(int value, int n, int row) {

        String result = new String();
        int quota = value;
        int rem = 0;

        if (n == 1) {
            n = 2;
        }

        while (quota != 0) {
            rem = quota % n;
            quota = (int) quota / n;
            result += Integer.toString(rem);
        }

        result = new StringBuffer(result).reverse().toString();

        return result;
    }

    public int[] transferToN(int value, int n, int row) {
        //자연수를 n진수로 변환하는 메소드.

        int quota = value;
        int rem = 0;
        if (n == 1) {
            n = 2;
        }

        Stack<Integer> stack = new Stack<>();
        int[] result = new int[row];

        while (quota != 0) {

            rem = quota % n;
            quota = (int) quota / n;
            stack.add(rem);

        }

        for (int i = 0; i < row; i++) {

            if (row - i > stack.size()) {

                result[i] = 0;

            } else {

                result[i] = stack.pop();

            }

        }

        return result;
    }

}
