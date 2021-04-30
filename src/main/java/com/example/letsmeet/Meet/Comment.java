package com.example.letsmeet.Meet;

import java.time.LocalDateTime;

import com.example.letsmeet.User.User;

import lombok.Data;

@Data
public class Comment {

	private String userId;
	private int userKey;
	private String content;
	private LocalDateTime created;
}
