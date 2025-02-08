package com.AlexandreLoiola.BatchHandler.repository;

import com.AlexandreLoiola.BatchHandler.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID>{

    Page<ProductModel> findByIsActive(Boolean isActive, Pageable pageable);

    Page<ProductModel> findByCategory(String category, Pageable pageable);
}
