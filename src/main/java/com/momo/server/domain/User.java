package com.momo.server.domain;

import com.momo.server.dto.UserInfoDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import lombok.Data;

@Data
@Document(collection = "user")
public class User {

    @Id
    private int _id;
    private String userId;
    private String meetId;
    private int[][] userTimes;

}
