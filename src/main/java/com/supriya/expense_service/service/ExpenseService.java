package com.supriya.expense_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.supriya.expense_service.dto.CategorySummaryResponse;
import com.supriya.expense_service.dto.ExpenseRequest;
import com.supriya.expense_service.dto.ExpenseResponse;
import com.supriya.expense_service.dto.MonthlySummaryResponse;
import com.supriya.expense_service.dto.RangeSummaryResponse;
import com.supriya.expense_service.entity.Category;
import com.supriya.expense_service.entity.Expense;
import com.supriya.expense_service.exception.ResourceNotFoundException;
import com.supriya.expense_service.exception.UnauthorizedActionException;
import com.supriya.expense_service.repository.CategoryRepository;
import com.supriya.expense_service.repository.ExpenseRepository;

@Service
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	
	private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public ExpenseResponse addExpense(ExpenseRequest request) {

        String email =
            SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()
                    )
                );

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(category)
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
                .category(expense.getCategory().getName())
                .expenseDate(expense.getExpenseDate())
                .build();
	}
	
	 public List<ExpenseResponse> getMyExpenses(String category, LocalDate fromDate, LocalDate toDate, BigDecimal minAmount, BigDecimal maxAmount) {

	        String email =
	            SecurityContextHolder.getContext()
	                .getAuthentication()
	                .getName();

	        return expenseRepository.findByUserEmail(email)
	                .stream()
	                .filter(e ->
	                        category == null || e.getCategory().getName().equalsIgnoreCase(category)
	                        )
	                .filter(e ->
	                        fromDate == null || !e.getExpenseDate().isBefore(fromDate)
	                        )
	                .filter(e ->
	                       toDate == null || !e.getExpenseDate().isAfter(toDate)
	                       )
	                .filter(e ->
	                       minAmount == null || e.getAmount().compareTo(minAmount) >= 0
	                       )
	                .filter(e ->
	                       maxAmount == null || e.getAmount().compareTo(maxAmount) <= 0)
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
	            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

	        if (!expense.getUserEmail().equals(loggedInEmail)
	                && !"ROLE_ADMIN".equals(role)) {
	            throw new UnauthorizedActionException("Not allowed to update this expense");
	        }
	        
	        Category category = categoryRepository.findById(request.getCategoryId())
	                .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                        "Category not found with id: " + request.getCategoryId()
	                    )
	                );

	        expense.setTitle(request.getTitle());
	        expense.setAmount(request.getAmount());
	        expense.setCategory(category);
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
	            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

	        if (!expense.getUserEmail().equals(loggedInEmail)
	                && !"ROLE_ADMIN".equals(role)) {
	            throw new AccessDeniedException("Not allowed to delete this expense");
	        }

	        expenseRepository.delete(expense);
	    }

	    public List<CategorySummaryResponse> getCategoryWiseSummary(){
	    	
	    	String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    	
	    	List<Expense> expenses = expenseRepository.findByUserEmail(email);
	    	
	    	return expenses.stream()
	    			.collect(Collectors.groupingBy(
	    					expense -> expense.getCategory().getName(), 
	    					Collectors.reducing(
	    							BigDecimal.ZERO,
	    							Expense::getAmount, 
	    							BigDecimal::add)))
                    .entrySet()
                    .stream()
                    .map(entry -> 
                            new CategorySummaryResponse(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
	    }
	    
	    public MonthlySummaryResponse getMonthlyWiseSummary(int year, int month){
	    	
	    	String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    	
	    	BigDecimal total = expenseRepository.findByUserEmail(email)
	                .stream()
	                .filter(e ->
	                        e.getExpenseDate().getYear() == year &&
	                        e.getExpenseDate().getMonthValue() == month
	                )
	                .map(Expense::getAmount)
	                .reduce(BigDecimal.ZERO, BigDecimal::add);
	    	
	    	return new MonthlySummaryResponse(year, month, total);
	    }
	    
	    public RangeSummaryResponse getDateRangeSummary(LocalDate from, LocalDate to) {
	    	
	    	String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    	
	    	BigDecimal total = expenseRepository.findByUserEmailAndExpenseDateBetween(email, from, to)
	    			.stream()
	    			.map(Expense::getAmount)
	    			.reduce(BigDecimal.ZERO, BigDecimal::add);
	    	
	    	return new RangeSummaryResponse(from.toString(), to.toString(), total);
	    }
	    
} 
