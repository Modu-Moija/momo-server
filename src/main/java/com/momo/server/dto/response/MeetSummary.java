package com.momo.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetSummary {

    private String[] who;
    private String[] when;
    private String where;

}
