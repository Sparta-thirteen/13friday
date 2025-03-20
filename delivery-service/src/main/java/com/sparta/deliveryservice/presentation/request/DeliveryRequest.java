package com.sparta.deliveryservice.presentation.request;



import com.sparta.deliveryservice.domain.model.DeliveryType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRequest {


    @NotBlank
    private UUID departure_hub_id;
    @NotBlank
    private UUID destination_hub_id;
    @NotBlank
    private UUID shipping_manager_id;
    @NotBlank
    private UUID shipping_manager_slack_id;
    @NotBlank
    private UUID company_delivery_manager_id;
    @NotBlank
    private String shipping_address;

    @Enumerated(EnumType.STRING)
    private DeliveryType delivery_status;




}
