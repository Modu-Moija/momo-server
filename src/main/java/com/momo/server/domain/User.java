package com.momo.server.domain;

import java.math.BigInteger;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Data;

@Data
@Document(collection = "user")
public class User {

//	@Temporal(TemporalType.TIMESTAMP) 
//	@DateTimeFormat(style = "M-") 
//	@CreatedDate
//	private Date createdDate; 

    @MongoId
    private ObjectId id;
    private BigInteger userId;
    private String username;
    private String meetId;
    private int[][] userTimes;
    private Boolean cookieRemember;

}