package com.supriya.expense_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RangeSummaryResponse {

	private String fromDate;
	private String toDate;
	private BigDecimal totalAmount;
}
