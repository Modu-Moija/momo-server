package com.example.letsmeet.Meet;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.letsmeet.Time.UserInfo;
import com.example.letsmeet.User.User;
import com.google.common.hash.Hashing;

@RestController
@RequestMapping(value="/api/meet")
public class MeetController {

	@Resource
	private UserInfo userInfo;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	Query query; 
	
	@PostMapping
	public ResponseEntity<String> newMeet(@RequestBody Meet meet) {
		//일정 생성. 
		
		LocalDate startDate = meet.getDates().get(0);
		LocalDate endDate = meet.getDates().get(1);
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		LocalDate curDate = startDate;
		
		while (!curDate.equals(endDate.plusDays(1))) {
			
			dates.add(curDate);
			curDate=curDate.plusDays(1);
			
		}
		
		meet.setDates(dates);
		
		String start = meet.getStart().split(":")[0];
		String end = meet.getEnd().split(":")[0];
		
		int col = Integer.parseInt(end) - Integer.parseInt(start);		
		col = (int)(60 / meet.getGap()) * col;
		int[] checkArray = new int[col];
		
		meet.setCheckArray(checkArray);
		meet.setCreated(LocalDateTime.now().plusHours(9));

		String newUrl = Hashing.sha256()
				  .hashString(meet.toString(), StandardCharsets.UTF_8)
				  .toString().substring(0,15);

		meet.setMeetId(newUrl);
		meet.setMeetSubInfo(new MeetSub(dates));
		
		mongoTemplate.insert(meet, "meet");
		
		return new ResponseEntity<>(newUrl,HttpStatus.OK);
	}
	
	@GetMapping(value="/info")
	public Meet MeetInfo(@RequestParam String id) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("meetId").is(id));
		User user = userInfo.getUser();
		
		Meet result = mongoTemplate.findOne(query, Meet.class, "meet"); 
		
		if(user != null) {
			result.setUserTime(user.getUserTimes());
		}
		
		return result;
	}
	
	@GetMapping
	public ResponseEntity<?> getMyMeet(HttpSession session){
		
		if(userInfo.getUser() == null) {
			return new ResponseEntity<String>("로그인 필요", HttpStatus.UNAUTHORIZED);
		}
		
		String meetId = userInfo.getUser().getMeetId();
		query = new Query();
		query.addCriteria(Criteria.where("meetId").is(meetId));
		
		return new ResponseEntity<Meet>(mongoTemplate.findOne(query, Meet.class, "meet"), HttpStatus.OK);
		
	}
	
	
	@PostMapping("/sub")
	public ResponseEntity<?> updateMeetSub(@RequestBody MeetSub meetSubInfo){
		//육하원칙 생성. 
		
		if(!User.checkUser(userInfo)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		String meetId = userInfo.getUser().getMeetId();
		
		query = new Query();
		query.addCriteria(Criteria.where("meetId").is(meetId));
		
		Update update = new Update();
		update.set("meetSubInfo.why", meetSubInfo.getWhy());
		update.set("meetSubInfo.how", meetSubInfo.getHow());
		update.set("meetSubInfo.what", meetSubInfo.getWhat());
		update.set("meetSubInfo.where", meetSubInfo.getWhere());
		
		FindAndModifyOptions option = new FindAndModifyOptions();
		option.returnNew(true);
		
		mongoTemplate.findAndModify(query, update, option, Meet.class, "meet");
		
		return ResponseEntity.ok().build();
		
	}
}
