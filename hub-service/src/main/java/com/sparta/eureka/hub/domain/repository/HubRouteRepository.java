package com.sparta.eureka.hub.domain.repository;

import com.sparta.eureka.hub.domain.entity.HubRoute;
import com.sparta.eureka.hub.infrastructure.queryDSL.hubRouteRepository.HubRouteRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID>, HubRouteRepositoryCustom {
    Page<HubRoute> findAllByIsDeletedIsFalse(Pageable pageable);

    Optional<HubRoute> findByDepartHub_HubIdAndArriveHub_HubId(UUID departHubHubId, UUID departHubHubId1);
}
