package com.sparta.deliveryservice.presentation.request;

import com.sparta.deliveryservice.domain.model.DeliveryType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateDeliveryRequest {

    @Enumerated(EnumType.STRING)
    private DeliveryType delivery_status;

}
