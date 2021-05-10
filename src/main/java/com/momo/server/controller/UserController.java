package com.momo.server.controller;

import com.momo.server.domain.User;
import com.momo.server.dto.UserInfoDto;
import javax.annotation.Resource;

import com.momo.server.domain.Meet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Resource
    private UserInfoDto userInfo;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Meet queryMeet;
    private String message;
    private HttpStatus status;
    private User queryUser;

    @PostMapping("signin")
    public ResponseEntity<?> signIn(@RequestBody User newbie) {

        //유저 검증.

        switch (checkUser(newbie)) {

            case 0:

                message = "해당 링크가 존재하지 않습니다.";
                status = HttpStatus.UNAUTHORIZED;

            case 2:

                message = "비밀번호가 일치하지 않습니다.";
                status = HttpStatus.UNAUTHORIZED;
                return new ResponseEntity<String>(message, status);

            case 1:

                //새로운 유저.
                int col = Integer.parseInt(queryMeet.getEnd().split(":")[0]) - Integer
                    .parseInt(queryMeet.getStart().split(":")[0]);
                col = (int) (60 / queryMeet.getGap()) * col;
                int row = queryMeet.getDates().size();

                int[][] userTime = new int[col][row];
                newbie.setUserTimes(userTime);
                newbie.setUserKey(Math.abs(newbie.hashCode()));
                mongoTemplate.insert(newbie, "user").hashCode();

                Query query = new Query();
                query.addCriteria(Criteria.where("meetId").is(newbie.getMeetId()));

                Update update = new Update();
                update.inc("num", 1);
                update.push("users", newbie.getUserKey());
                update.push("meetSubInfo.who", newbie.getUserId());

                FindAndModifyOptions option = new FindAndModifyOptions();
                option.returnNew(true);

                Meet result = (Meet) mongoTemplate
                    .findAndModify(query, update, option, Meet.class, "meet");
                result.setUserTime(newbie.getUserTimes());

                userInfo.setUser(newbie);
                message = "아이디 생성 완료.";
                status = HttpStatus.CREATED;

                return new ResponseEntity<Meet>(result, status);

            case 3:

                Meet meet = User.getMeet(mongoTemplate, newbie.getMeetId());
                message = "로그인 완료";
                meet.setUserTime(queryUser.getUserTimes());
                status = HttpStatus.OK;
                userInfo.setUser(queryUser);

                return new ResponseEntity<Meet>(meet, status);
        }

        userInfo.setMeetId(queryMeet.getMeetId());
        userInfo.setGap(queryMeet.getGap());
        userInfo.setDates(queryMeet.getDates());

        return new ResponseEntity<String>(message, status);

    }

    public int checkUser(User user) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));

        queryMeet = mongoTemplate.findOne(query, Meet.class, "meet");

		if (queryMeet == null) {
			return 0;
		}

        query.addCriteria(Criteria.where("userId").is(user.getUserId()));

        queryUser = mongoTemplate.findOne(query, User.class, "user");

		if (queryUser == null) {
			return 1;
		}

        query.addCriteria(Criteria.where("userPass").is(user.getUserPass()));

        queryUser = mongoTemplate.findOne(query, User.class, "user");

		if (queryUser == null) {
			return 2;
		}

        return 3;

    }
}
