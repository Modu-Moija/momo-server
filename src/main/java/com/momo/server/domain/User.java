package com.momo.server.domain;

<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.momo.server.dto.UserInfoDto;
import io.swagger.annotations.ApiModelProperty;
>>>>>>> 4355ac99473f025c33d993f8d1bdde7557ca9caa
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Document(collection = "user")
public class User {

    private BigInteger userId;
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
