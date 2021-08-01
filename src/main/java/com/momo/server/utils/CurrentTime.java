package com.momo.server.utils;

import java.time.LocalDateTime;

public class CurrentTime {


    public static LocalDateTime getCurrentTime() {

        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime;
    }

}
