package com.momo.server.service;

import com.momo.server.domain.User;
import com.momo.server.exception.MeetDoesNotExistException;
import com.momo.server.repository.UserRepository;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    //로그인 메소드
    public ResponseEntity<?> login(User user) {

        boolean isUserExist;

        try{
            isUserExist = userRepository.isUserExist(user);
        }catch (MeetDoesNotExistException e){
            return ResponseEntity.noContent().build();
        }

        if (isUserExist){
            return ResponseEntity.ok().build();
        }else{
            return createUser(user);
        }
    }

    //유저 생성
    public ResponseEntity<?> createUser(User user) {
        userRepository.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //유저의 시간정보 저장
    public ResponseEntity<?> updateUserTimetable() {
        userRepository.updateUserTimetable();
        return ResponseEntity.ok().build();
    }

    //유저이름 찾는 메소드
    public ArrayList findUsername() {
        userRepository.findUsername();
        return null;
    }
}
