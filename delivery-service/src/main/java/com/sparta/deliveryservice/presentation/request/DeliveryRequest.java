package com.sparta.deliveryservice.presentation.request;



import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRequest {


    @NotBlank
    private UUID orderId;
    @NotBlank
    private UUID recipientsId;
    @NotBlank
    private String shippingAddress;

//
//    @Enumerated(EnumType.STRING)
//    private DeliveryType deliveryStatus;




}
