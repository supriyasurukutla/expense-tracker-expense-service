package com.supriya.expense_service.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

	     @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String title;

	    @Column(nullable = false)
	    private BigDecimal amount;

	    @ManyToOne
	    @JoinColumn(name = "category_id", nullable = false)
	    private Category category;

	    @Column(nullable = false)
	    private LocalDate expenseDate;

	    @Column(nullable = false)
	    private String userEmail;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		public LocalDate getExpenseDate() {
			return expenseDate;
		}

		public void setExpenseDate(LocalDate expenseDate) {
			this.expenseDate = expenseDate;
		}

		public String getUserEmail() {
			return userEmail;
		}

		public void setUserEmail(String userEmail) {
			this.userEmail = userEmail;
		}
	    
	    
}
