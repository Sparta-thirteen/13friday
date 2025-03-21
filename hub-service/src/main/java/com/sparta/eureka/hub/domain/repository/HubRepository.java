package com.sparta.eureka.hub.domain.repository;

import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.infrastructure.queryDSL.hubRepository.HubRepositoryCustom;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
    boolean existsByHubName(@NotBlank String hubName);
    Page<Hub> findAllByIsDeletedFalse(Pageable pageable);
}
