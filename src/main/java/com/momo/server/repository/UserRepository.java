package com.momo.server.repository;

import com.momo.server.domain.User;
import com.momo.server.dto.request.LoginRequestDto;

public interface UserRepository {

    // 유저 생성 메소드
    void createUser(User user);

    void deleteUser();

    User isUserExist(LoginRequestDto loginRequestDto);

    User findUser(User user);

    void updateUserTime(User user, int[][] temp_userTimes, int[][] temp_Times);

    void findAndRemoveUser(String userId);

}
