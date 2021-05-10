package com.momo.server.Meet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.User.UserInfo;
import com.momo.server.User.User;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private MongoTemplate mongoTemplate;

    private User user;
    private ResponseEntity response;

    @Resource
    private UserInfo userInfo;

    @PostMapping
    public ResponseEntity<?> newComment(@RequestBody Comment comment) {
        //댓글 생성 api.
        //이미 있으면 수정, 없으면 새로 생성.

        user = userInfo.getUser();

        comment.setCreated(LocalDateTime.now());
        comment.setUserKey(user.getUserKey());
        comment.setUserId(user.getUserId());

        Query query = new Query();
        query.addCriteria(Criteria.where("userKey").is(user.getUserKey()));

        if (!User.checkUser(userInfo)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Update update = new Update();
        update.set("content", comment.getContent());
        update.set("userId", comment.getUserId());
        update.set("created", comment.getCreated());
        update.set("meetId", user.getMeetId());
        mongoTemplate.upsert(query, update, Comment.class);

        return ResponseEntity.ok().build();

    }

    @GetMapping
    public List<Comment> getMeets() {
        //해당하는 약속에 있는 모든 댓글들을 불러옴.

        user = userInfo.getUser();
        List<Comment> results = new ArrayList<Comment>();

        if (user != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("meetId").is(user.getMeetId()));

            Map comments = new HashMap<String, String>();
            results = mongoTemplate.find(query, Comment.class, "comment");

        }

        return results;

    }
}
