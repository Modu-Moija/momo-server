package com.momo.server.dto.auth;

import com.momo.server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class SessionUser implements Serializable {

    private BigInteger userId;
    private String username;
    private String meetId;
    private int[][] userTimes;
    private Boolean cookieRemember;
    private LocalDateTime created;


    public SessionUser(User user) {
        this.username = user.getUsername();
        this.userId=user.getUserId();
        this.userTimes=user.getUserTimes();
        this.meetId=user.getMeetId();
        this.created=user.getCreated();
        this.cookieRemember=user.getCookieRemember();
    }

}