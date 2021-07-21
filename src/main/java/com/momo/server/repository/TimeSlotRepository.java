package com.momo.server.repository;

import java.util.ArrayList;

import com.momo.server.domain.TimeSlot;

public interface TimeSlotRepository {

    void createTimeSlot(ArrayList<TimeSlot> timeSlotList);
}
