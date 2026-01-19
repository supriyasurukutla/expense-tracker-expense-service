package com.supriya.expense_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supriya.expense_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByNameIgnoreCase(String name);
	
	List<Category> findByActiveTrue();
}
