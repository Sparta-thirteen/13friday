package com.sparta.deliveryservice.domain.model;

import com.sparta.deliveryservice.application.dto.DeliveryRouteDto;

public enum DeliveryRouteType {
    HUB_WAITING,        // 허브 대기중
    HUB_PROGRESS,       // 허브 이동중
    HUB_DELIVERED,      // 허브로 도착
    COMPANY_PROGRESS,   // 업체 배송중
    COMPANY_DELIVERED;   // 업체 배송완료



    public DeliveryRouteDto createDtoFromBase(DeliveryRouteDto dto) {
        if (this == HUB_WAITING) {
            return new DeliveryRouteDto(
                dto.getDepartureHubId(),
                dto.getDestinationHubId(),
                dto.getDeliveryId(),
                dto.getShippingAddress(),
                0L,
                null,
                0L,
                null
            );
        }

        return dto;
    }
}



