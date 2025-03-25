package com.sparta.deliveryservice.presentation.response;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryCreatedResponse {

    @NotBlank
    private UUID id;
}
