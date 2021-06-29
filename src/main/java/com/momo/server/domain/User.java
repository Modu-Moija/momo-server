package com.momo.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Builder;
import lombok.Data;

@Data
@Document(collection = "user")
public class User {

    @Id
    private int _id;
    private String username;
    private String meetId;
    private int[][] userTimes;
    
    
    @Builder
	public User(int _id, String username, String meetId, int[][] userTimes) {
		this._id = _id;
		this.username = username;
		this.meetId = meetId;
		this.userTimes = userTimes;
	}

}
