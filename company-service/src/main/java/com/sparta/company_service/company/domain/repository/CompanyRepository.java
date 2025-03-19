package com.sparta.company_service.company.domain.repository;

import com.sparta.company_service.company.domain.entity.Company;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

  @Query("SELECT c FROM Company c WHERE c.name LIKE %:keyword% AND c.deletedAt IS NULL ORDER BY c.createdAt DESC, c.updatedAt DESC")
  Page<Company> findAllByName(String keyword, Pageable pageable);

  Page<Company> findByDeletedAtIsNull(Pageable pageable);
}
