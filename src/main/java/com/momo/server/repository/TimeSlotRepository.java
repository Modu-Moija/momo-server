package com.momo.server.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.momo.server.domain.TimeSlot;

public interface TimeSlotRepository {

    void createTimeSlot(ArrayList<TimeSlot> timeSlotList);

    void findAllAndRemoveTimeSlot(String meetId);

    List<TimeSlot> findAllTimeSlot(String meetId);

    void updateTimeSlot(String meetId, HashSet<String> users, LocalDate date, String time);

}
