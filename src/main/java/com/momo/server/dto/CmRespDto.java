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

    private ResponseEntity<?> code;
    private String message;
    private T data;
}
