package com.supriya.expense_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseRequest {

	private String title;
	private BigDecimal amount;
	private String category;
	private LocalDate expenseDate;
}
