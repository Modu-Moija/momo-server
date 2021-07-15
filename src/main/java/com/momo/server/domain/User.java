package com.momo.server.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.math.BigInteger;

@Data
@Document(collection = "user")
public class User {

	
	
//	@Temporal(TemporalType.TIMESTAMP) 
//	@DateTimeFormat(style = "M-") 
//	@CreatedDate
//	private Date createdDate; 
	
    private BigInteger userId;
    private String username;
    private String meetId;
    private int[][] userTimes;

}