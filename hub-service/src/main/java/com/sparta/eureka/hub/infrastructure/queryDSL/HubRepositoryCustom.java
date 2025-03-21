package com.sparta.eureka.hub.infrastructure.queryDSL;

import com.sparta.eureka.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRepositoryCustom {
    Page<Hub> searchByKeyword(String keyword, boolean isDesc, Pageable pageable);
}
