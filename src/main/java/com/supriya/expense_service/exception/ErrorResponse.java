package com.supriya.expense_service.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

	private LocalDateTime timestamp;
	private int status;
	private String message;
	private String error;
	private String path;
}
