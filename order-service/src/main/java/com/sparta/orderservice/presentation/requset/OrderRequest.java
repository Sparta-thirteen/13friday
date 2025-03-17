package com.sparta.orderservice.presentation.requset;


import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

    @NotEmpty
    private UUID suppliersId;
    @NotEmpty
    private UUID recipientsId;
    @NotEmpty
    private UUID deliveryId;
    private String requestDetails;
    private List<OrderItemsRequest> orderItemsRequests;

}

