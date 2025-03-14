package com.sparta.company_service.company.repository;

import com.sparta.company_service.company.entity.Company;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

}
