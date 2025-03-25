package com.sparta.eureka.hub.domain.entity;

import com.sparta.eureka.hub.infrastructure.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@Table(name = "p_hub")
@NoArgsConstructor
@AllArgsConstructor
public class Hub extends Auditable {
    @Id
    @GeneratedValue
    private UUID hubId;

    private Long hubUserId;

    @Column(nullable = false, unique = true)
    private String hubName;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(precision = 9, scale = 6)
    private BigDecimal lat;

    @Column(precision = 9, scale = 6)
    private BigDecimal lon;

    public static Hub create(String hubName, String address) {

        return Hub.builder()
                .hubName(hubName)
                .address(address)
                .build();
    }

    public void update(String hubName, String address) {
        this.hubName = hubName;
        this.address = address;
    }

    public void latAndLon (BigDecimal lat, BigDecimal lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void updateUserId(Long userId) {
        this.hubUserId = userId;
    }

    public void delete(Long userId) {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeleted(true);
        this.setDeletedBy(userId);
    }

}
