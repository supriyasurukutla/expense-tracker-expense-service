package com.supriya.expense_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supriya.expense_service.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	List<Expense> findByUserEmail(String userEmail);
	List<Expense> findByUserEmailAndExpenseDateBetween(String userEmail, LocalDate from, LocalDate to);

}
