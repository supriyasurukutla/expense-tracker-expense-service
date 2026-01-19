package com.supriya.expense_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummaryResponse {

	private int year;
	private int month;
	private BigDecimal totalAmount;
	
	
}
