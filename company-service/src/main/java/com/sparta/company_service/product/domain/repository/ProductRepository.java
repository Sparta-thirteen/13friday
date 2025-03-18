package com.sparta.company_service.product.domain.repository;


import com.sparta.company_service.product.domain.entity.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
  Page<Product> findAllByName(String keyword, Pageable pageable);
}
