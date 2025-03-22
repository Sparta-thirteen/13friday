package com.sparta.orderservice.presentation.requset;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private String requestDetails;
    @NotEmpty
    private List<OrderItemsRequest> orderItemsRequests;

}

