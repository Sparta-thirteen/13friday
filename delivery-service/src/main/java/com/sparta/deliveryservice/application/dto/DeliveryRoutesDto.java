package com.sparta.deliveryservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRoutesDto {

    @NotBlank
    private String recipientsSlackId;
    @NotBlank
    private UUID companyDeliveryManagerId;


}
