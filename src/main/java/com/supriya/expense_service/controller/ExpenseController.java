package com.supriya.expense_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supriya.expense_service.dto.ExpenseRequest;
import com.supriya.expense_service.dto.ExpenseResponse;
import com.supriya.expense_service.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

	private final ExpenseService expenseService;
	
	public ExpenseController(ExpenseService expenseService) {
		
		this.expenseService = expenseService;
		
	}
	
	@PostMapping	
	public ResponseEntity<ExpenseResponse> addExpense(@RequestBody ExpenseRequest request) {
		
	     return ResponseEntity.ok(expenseService.addExpense(request));
	}
	
	@GetMapping
	public ResponseEntity<List<ExpenseResponse>> getMyExpenses() {
		
		return ResponseEntity.ok(expenseService.getMyExpenses());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
		return ResponseEntity.ok(expenseService.getAllExpenses());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ExpenseResponse> updateExpense(
	        @PathVariable Long id,
	        @RequestBody ExpenseRequest request) {
	    return ResponseEntity.ok(expenseService.updateExpense(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
	    expenseService.deleteExpense(id);
	    return ResponseEntity.noContent().build();
	}

}
