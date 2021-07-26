package com.momo.server.domain;

import java.time.LocalDate;
import java.util.HashSet;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "timeslot")
public class TimeSlot implements Comparable<TimeSlot> {

    @MongoId
    private ObjectId id;
    private String meetId;
    private HashSet<String> users;
    private String time;
    private LocalDate date;
    private Integer num;

    @Override
    public int compareTo(TimeSlot o) {
	int res = o.getNum().compareTo(this.getNum());
	if (res == 0) {
	    res = o.getDate().compareTo(this.getDate());
	} else if (res == 0) {
	    res = o.getTime().compareTo(this.getTime());
	}
	// num순 정렬
	return res;
    }

}