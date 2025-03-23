package com.sparta.orderservice.presentation.requset;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateOrderRequest {

    private String requestDetails;
    @NotEmpty
    @Schema(hidden = true)
    private List<OrderItemsRequest> orderItemsRequests;

}
