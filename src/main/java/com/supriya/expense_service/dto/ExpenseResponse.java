package com.supriya.expense_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseResponse {

	private Long id;
    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;
}
