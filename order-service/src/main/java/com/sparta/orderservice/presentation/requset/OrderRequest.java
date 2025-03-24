package com.sparta.orderservice.presentation.requset;


import io.swagger.v3.oas.annotations.media.Schema;
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
    private String suppliersName;
    @NotEmpty
    private String email;
    @NotEmpty
    private String recipientsName;
    @NotEmpty
    private String requestDetails;
    @NotEmpty
    private List<OrderItemsRequest> orderItemsRequests;



}

