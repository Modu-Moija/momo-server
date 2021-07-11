package com.momo.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.momo.server.dto.UserInfoDto;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

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