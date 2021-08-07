package com.momo.server.repository;

import com.momo.server.domain.User;
import com.momo.server.dto.auth.SessionUser;
import com.momo.server.dto.request.LoginRequestDto;

public interface UserRepository {

    // 유저 생성 메소드
    void createUser(User user);

    void deleteUser();

    User isUserExist(LoginRequestDto loginRequestDto);

    User findUser(SessionUser user);

    void updateUserTime(SessionUser user, int[][] temp_userTimes);

    void removeUserByUsername(String username);

}
