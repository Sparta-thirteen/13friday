package com.sparta.deliveryservice.domain.model;

public enum DeliveryRouteType {
    HUB_WAITING,        // 허브 대기중
    HUB_PROGRESS,       // 허브 이동중
    HUB_DELIVERED,      // 허브로 도착
    COMPANY_PROGRESS,   // 업체 배송중
    COMPANY_DELIVERED   // 업체 배송완료
}

