package com.sparta.eureka.hub.domain.repository;

import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.infrastructure.queryDSL.HubRepositoryCustom;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
    boolean existsByHubName(@NotBlank String hubName);
}
