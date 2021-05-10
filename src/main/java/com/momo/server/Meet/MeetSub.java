package com.momo.server.Meet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MeetSub {

    private ArrayList<String> who;
    private String when;
    private String why;
    private String what;
    private String where;
    private String how;

    MeetSub(ArrayList<LocalDate> dates) {

        LocalDate date = dates.get(0);
        int index = dates.size() - 1;

        String startDate = date.format(DateTimeFormatter.ofPattern("MM/dd", Locale.KOREA));
        date = dates.get(index);
        String endDate = date.format(DateTimeFormatter.ofPattern("MM/dd", Locale.KOREA));

        this.when = startDate + " ~ " + endDate;
        this.who = new ArrayList<String>();

    }

    public void addUser(String name) {
        who.add(name);
    }
}
