package com.AlexandreLoiola.BatchHandler.repository;

import com.AlexandreLoiola.BatchHandler.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID>{

    Page<ProductModel> findByIsActive(Boolean isActive, Pageable pageable);

    Optional<ProductModel> findProductByName(String name);
}
