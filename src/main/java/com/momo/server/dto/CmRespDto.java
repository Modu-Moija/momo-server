package com.momo.server.dto;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmRespDto<T> {
//공통된 response dto
	
	private ResponseEntity<?> code;//1은 성공, -1은 실패
	private String message;
	private T data;
}
