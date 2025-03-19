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

    @Column(nullable = false, unique = true)
    private String hubName;

    @Column(nullable = false, unique = true)
    private String address;

    @Column
    private BigDecimal lat;

    @Column
    private BigDecimal lon;

    @Column
    private boolean isDeleted = false;

    public static Hub create(String hubName, String address) {

        return Hub.builder()
                .hubName(hubName)
                .address(address)
                .lat(null)
                .lon(null)
                .isDeleted(false)
                .build();
    }

    public void update(String hubName, String address, BigDecimal lat, BigDecimal lon) {
        this.hubName = hubName;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public void latAndLon (BigDecimal lat, BigDecimal lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void delete() {
        this.setDeletedAt(LocalDateTime.now());
        this.isDeleted = true;
    }

}
