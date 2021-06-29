package com.momo.server.repository.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {


    private final MongoTemplate mongoTemplate;

    @Override
    public int createUser(User user) {
        return 0;
    }

    @Override
    public boolean isUserExist(User user) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));
        query.addCriteria(Criteria.where("userId").is(user.getUsername()));

        return mongoTemplate.findOne(query,User.class, "user") != null;
    }

	@Override
    public void updateUserTime(User user, UserTimeUpdateRequestDto requestDto) {
		
		//DB에서 유저 찾기
		Query query = new Query();
		User dbuser = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(user.get_id())), User.class);
		
		
		//DB에서 meet 찾기
		Meet dbmeet = mongoTemplate.findOne(Query.query(Criteria.where("meetid").is(user.getMeetId())), Meet.class);
		
		
		//i,j를 찾는다
		
		//dbmeet로 column 위치 계산
		ArrayList dates = dbmeet.getDates();
		
		int x=0;
		for(int i =0; i<dates.size();i++) {
			if(requestDto.getDate().equals(dates.get(i))) {
				x=i;
				break;
			}
		}
		
		
		//dbmeet로 row 위치 계산
		int y=0;
		String start = dbmeet.getStart();
		int hour = Integer.parseInt(start.substring(0,1));
		int min = Integer.parseInt(start.substring(3,4));
		
		
		int total_hour = hour*60+min;
		
		String timeslot = requestDto.getTimeslot();
		int input_hour = Integer.parseInt(timeslot.substring(0,1));
		int input_min = Integer.parseInt(timeslot.substring(3,4));
		
		int input_total_hour = input_hour*60+input_min;
		
		int gap = dbmeet.getGap();
		
		y=(total_hour-input_total_hour)/gap;
		
		
		//좌표값 1로 세팅
		int[][] temp_userTimes = dbuser.getUserTimes();
		temp_userTimes[y][x]=1;
		
		//db에 저장
		dbuser.setUserTimes(temp_userTimes);
		mongoTemplate.save(dbuser);
    
		
    }

    @Override
    public void findUsername() {

    }



}
