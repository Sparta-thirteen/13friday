package com.sparta.company_service.product.domain.repository;


import com.sparta.company_service.product.domain.entity.Product;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.deletedAt IS NULL "
      + "ORDER BY p.createdAt DESC, p.updatedAt DESC")
  Page<Product> findAllByName(String keyword, Pageable pageable);

  Page<Product> findByDeletedAtIsNull(Pageable pageable);

  // todo: update deletedBy 추가
  @Modifying
  @Query("UPDATE Product p SET p.deletedAt = :deletedAt "
      + "WHERE p.companyId = :companyId")
  void softDeleteByCompanyId(@Param("companyId") UUID companyId,
      @Param("deletedAt") LocalDateTime deletedAt);
}
