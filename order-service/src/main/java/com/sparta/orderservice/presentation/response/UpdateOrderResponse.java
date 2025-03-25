package com.sparta.orderservice.presentation.response;


import com.sparta.orderservice.application.dto.OrderItemsDto;
import com.sparta.orderservice.domain.model.OrderItems;
import com.sparta.orderservice.presentation.requset.OrderItemsRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderResponse {

    private int totalStock;
    private String requestDetails;
    @NotEmpty
    private List<OrderItemsDto> orderItemsRequests;

}
