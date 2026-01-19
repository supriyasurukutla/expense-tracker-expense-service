package com.supriya.expense_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategorySummaryResponse {

	private String category;
	private BigDecimal totalAmount;
}
