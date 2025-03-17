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

  // todo: deletedAt null check, order by check
  @Query("SELECT c FROM Company c WHERE c.name LIKE %:keyword%")
  Page<Company> findAllByName(String keyword, Pageable pageable);
}
