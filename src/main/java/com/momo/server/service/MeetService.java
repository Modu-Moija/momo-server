package com.momo.server.service;

import com.momo.server.repository.MeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetService {

    @Autowired
    private MeetRepository meetRepository;

}
