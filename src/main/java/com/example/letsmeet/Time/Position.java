package com.example.letsmeet.Time;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Position {

	int x;
	int y;
	
	public static String pos2str(int xx, int yy) {
		
		return String.valueOf(xx)+","+String.valueOf(yy);
	}
	
}
