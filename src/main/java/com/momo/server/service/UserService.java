package com.momo.server.service;

import com.momo.server.domain.User;
import com.momo.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    //유저 생성
    public ResponseEntity<?> createUser() {
        userRepository.createUser();
        return ResponseEntity.ok().build();
    }

    //유저의 시간정보 저장
    public ResponseEntity<?> createUserDatetime() {
        userRepository.createUserDatetime();
        return ResponseEntity.ok().build();
    }
}
