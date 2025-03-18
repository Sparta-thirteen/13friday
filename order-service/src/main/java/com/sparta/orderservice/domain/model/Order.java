package com.sparta.orderservice.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name ="p_order")
@Getter
@RequiredArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID suppliers_id;
    private UUID recipients_id;
    private UUID product_id;
    private UUID delivery_id;
    private int stock;
    private String request_details;


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


