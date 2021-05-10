package com.momo.server.domain;

import com.momo.server.dto.UserInfoDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import lombok.Data;

@Data
public class User {

    @Id
    private int userKey;
    private String userId;
    private String userPass;
    private String meetId;
    private int[][] userTimes;

    public static boolean checkUser(UserInfoDto userInfo) {

        if (userInfo.getUser() == null || userInfo.getUser().getUserId() == null) {
            return false; //로그인 안 되어 있음.
        } else {
            return true;
        }
    }

    public static Meet getMeet(MongoTemplate mongoTemplate, String meetId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("meetId").is(meetId));

        return mongoTemplate.findOne(query, Meet.class);
    }

    public static User getUser(MongoTemplate mongoTemplate, String userId, String meetId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, User.class);

    }
}
