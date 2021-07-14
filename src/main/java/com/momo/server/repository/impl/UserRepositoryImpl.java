package com.momo.server.repository.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {


    private final MongoTemplate mongoTemplate;

    @Transactional
    @Override
    public void createUser(User user) {
        BigInteger userid = BigInteger.valueOf(Integer.valueOf(Math.abs(user.hashCode())));
        
        
        user.setUserId(userid);
        Meet meet = getUserMeet(user.getMeetId());
        
        
        int dates = meet.getDates().size();
        int timeslots = Integer.parseInt(meet.getEnd().split(":")[0]) - Integer.parseInt(meet.getStart().split(":")[0]);
        int[][] userTimes = new int[timeslots*((int) 60/meet.getGap())][dates];
        user.setUserTimes(userTimes);
        mongoTemplate.insert(user, "user");
    }

    @Override
    public boolean isUserExist(User user) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));
        query.addCriteria(Criteria.where("username").is(user.getUsername()));
        return mongoTemplate.findOne(query,User.class, "user") != null;
    }

	@Override
    public void updateUserTime(User user, UserTimeUpdateRequestDto requestDto) {
		
		
		User dbuser = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(user.getUserId())), User.class);
		Meet dbmeet = mongoTemplate.findOne(Query.query(Criteria.where("meetId").is(user.getMeetId())), Meet.class);
		
		//dbmeet로 column 위치 계산
		ArrayList<LocalDate> dates = dbmeet.getDates();
		
		int x=0;
		for(int i =0; i<dates.size();i++) {
//			System.out.println(requestDto.getDate());
//			System.out.println(dates.get(i));
			if(requestDto.getDate().equals(dates.get(i))) {
				x=i;
				break;
			}
		}
		x=x+1;
		
		//dbmeet로 row 위치 계산
		int y=0;
		String start = dbmeet.getStart();
		//System.out.println(start);
		int hour = Integer.parseInt(start.substring(0,2));
		
		//System.out.println(hour);
		int min = Integer.parseInt(start.substring(3,5));
		//System.out.println(min);
		
		
		int total_hour = hour*60+min;
		//System.out.println(total_hour);
		
		
		String timeslot = requestDto.getTimeslot();
		
		//System.out.println(timeslot);
		int input_hour = Integer.parseInt(timeslot.substring(0,2));
		
		//System.out.println(input_hour);
		int input_min = Integer.parseInt(timeslot.substring(3,5));
		//System.out.println(input_min);
		
		
		int input_total_hour = input_hour*60+input_min;
		//System.out.println(input_total_hour);
		int gap = dbmeet.getGap();
		//System.out.println(gap);
		
		y=(input_total_hour-total_hour)/gap;
		System.out.println(y);
		
		
		//usertimetable 불러오기
		int[][] temp_userTimes = dbuser.getUserTimes();
		
		//meettimetable 불러오기
		int[][] temp_Times = dbmeet.getTimes();
		
		//true일 때 좌표값 1로 세팅,false일때 좌표값 0으로 세팅
		if(requestDto.isPossible()==true) {
			temp_userTimes[y][x]=1;
			temp_Times[y][x]=temp_Times[y][x]+1;
		}
		else if(requestDto.isPossible()==false) {
			temp_userTimes[y][x]=0;
			temp_Times[y][x]=temp_Times[y][x]-1;
		}
		

		dbuser.setUserTimes(temp_userTimes);
		dbmeet.setTimes(temp_Times);
		
		//DB에서 user 찾기
		Query query = new Query(Criteria.where("userId").is(user.getUserId()));
		Update update = new Update();
	    update.set("userTimes", temp_userTimes);
	    mongoTemplate.updateFirst(query, update, User.class);
	   
	    
		//DB에서 meet 찾기
	    Query meetQuery = new Query(Criteria.where("meetId").is(user.getMeetId()));
		Update meetUpdate = new Update();
		meetUpdate.set("times", temp_Times);
	    mongoTemplate.updateFirst(meetQuery, meetUpdate, Meet.class);
	    
		
    }

	@Transactional
    @Override
    public void increaseMeetNum(String meetId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(meetId));
        Meet targetMeet = mongoTemplate.findOne(query, Meet.class, "meet");


    }

    //유저가 속한 Meet 불러오는 메소드
    @Transactional(readOnly = true)
    @Override
    public Meet getUserMeet(String meetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(meetId));
        return mongoTemplate.findOne(query, Meet.class, "meet");
    }
    
    
    //유저아이디로 User 객체 불러오는 메소드
    @Transactional(readOnly = true)
	@Override
    public User getUser(BigInteger userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, User.class, "user");
    }


}
