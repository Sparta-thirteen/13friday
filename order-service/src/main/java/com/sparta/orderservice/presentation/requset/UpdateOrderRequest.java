package com.sparta.orderservice.presentation.requset;


import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateOrderRequest {

    private String requestDetails;
    @NotEmpty
    private List<OrderItemsRequest> orderItemsRequests;

}
