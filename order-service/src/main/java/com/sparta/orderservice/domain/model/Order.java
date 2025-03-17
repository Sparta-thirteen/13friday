package com.sparta.orderservice.domain.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name ="p_order")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID suppliersId;
    private UUID recipientsId;
    private UUID deliveryId;
    private int stock;
    private String requestDetails;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems = new ArrayList<>();

    public Order(UUID suppliersId, UUID recipientsId, UUID deliveryId, int stock, String requestDetails, List<OrderItems> orderItems) {
        this.suppliersId =suppliersId;
        this.recipientsId = recipientsId;
        this.deliveryId =deliveryId;
        this.stock = stock;
        this.requestDetails =requestDetails;
        this.orderItems = orderItems;
    }

//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime modifiedAt;
//
//    @Column(nullable = false, columnDefinition = "boolean default false")
//    private boolean isDeleted;
//
//    private LocalDateTime deletedAt;
//
//    @CreatedBy
//    private String createdBy;
//
//    @LastModifiedBy
//    private String modifiedBy;
//
//    private String deletedBy;
//
//    @PreUpdate
//    public void setDeleted() {
//        if (isDeleted) {
//            deletedAt = LocalDateTime.now();
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated()) {
//                deletedBy = "SYSTEM";
//                return;
//            }
//
//            Object principal = authentication.getPrincipal();
//
//            if (principal instanceof UserDetails) {
//                deletedBy = ((UserDetails) principal).getUsername();
//                return;
//            }
//
//            deletedBy = "UNKNOWN";
//        }
//    }

}


