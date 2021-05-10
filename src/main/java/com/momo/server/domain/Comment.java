package com.momo.server.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comment {

    private String userId;
    private int userKey;
    private String content;
    private LocalDateTime created;
}
