package com.supriya.expense_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.supriya.expense_service.dto.ExpenseRequest;
import com.supriya.expense_service.dto.ExpenseResponse;
import com.supriya.expense_service.entity.Expense;
import com.supriya.expense_service.repository.ExpenseRepository;

@Service
public class ExpenseService {

	private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseResponse addExpense(ExpenseRequest request) {

        String email =
            SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(request.getCategory())
                .expenseDate(request.getExpenseDate())
                .userEmail(email)
                .build();

        Expense saved = expenseRepository.save(expense);

        return mapToResponse(saved);
    }

	private ExpenseResponse mapToResponse(Expense expense) {
	
		return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .expenseDate(expense.getExpenseDate())
                .build();
	}
	
	 public List<ExpenseResponse> getMyExpenses() {

	        String email =
	            SecurityContextHolder.getContext()
	                .getAuthentication()
	                .getName();

	        return expenseRepository.findByUserEmail(email)
	                .stream()
	                .map(this::mapToResponse)
	                .collect(Collectors.toList());
	    }

	    
	    public List<ExpenseResponse> getAllExpenses() {

	        return expenseRepository.findAll()
	                .stream()
	                .map(this::mapToResponse)
	                .collect(Collectors.toList());
	    }
	    
	    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {

	        String loggedInEmail =
	            SecurityContextHolder.getContext()
	                .getAuthentication()
	                .getName();

	        String role =
	            SecurityContextHolder.getContext()
	                .getAuthentication()
	                .getAuthorities()
	                .stream()
	                .findFirst()
	                .map(a -> a.getAuthority())
	                .orElse("");

	        Expense expense = expenseRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Expense not found"));

	        if (!expense.getUserEmail().equals(loggedInEmail)
	                && !"ROLE_ADMIN".equals(role)) {
	            throw new AccessDeniedException("Not allowed to update this expense");
	        }

	        expense.setTitle(request.getTitle());
	        expense.setAmount(request.getAmount());
	        expense.setCategory(request.getCategory());
	        expense.setExpenseDate(request.getExpenseDate());

	        Expense updated = expenseRepository.save(expense);

	        return mapToResponse(updated);
	    }
	    
	    public void deleteExpense(Long id) {

	        String loggedInEmail =
	            SecurityContextHolder.getContext()
	                .getAuthentication()
	                .getName();

	        String role =
	            SecurityContextHolder.getContext()
	                .getAuthentication()
	                .getAuthorities()
	                .stream()
	                .findFirst()
	                .map(a -> a.getAuthority())
	                .orElse("");

	        Expense expense = expenseRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Expense not found"));

	        if (!expense.getUserEmail().equals(loggedInEmail)
	                && !"ROLE_ADMIN".equals(role)) {
	            throw new AccessDeniedException("Not allowed to delete this expense");
	        }

	        expenseRepository.delete(expense);
	    }

}
