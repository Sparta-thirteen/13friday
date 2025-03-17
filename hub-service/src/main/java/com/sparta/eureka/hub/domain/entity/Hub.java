package com.sparta.eureka.hub.domain.entity;

import jakarta.persistence.*;

import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@Table(name = "p_hub")
@NoArgsConstructor
@AllArgsConstructor
public class Hub {
    @Id
    @GeneratedValue
    private UUID hubId;

    @Column
    private String hubName;

    @Column
    private String address;

    @Column
    private BigDecimal lat;

    @Column
    private BigDecimal lon;

    private boolean isDeleted;

    public static Hub create(String hubName, String address, BigDecimal lat, BigDecimal lon) {

        return Hub.builder()
                .hubName(hubName)
                .address(address)
                .lat(lat)
                .lon(lon)
                .isDeleted(false)
                .build();
    }

    public void update(String hubName, String address, BigDecimal lat, BigDecimal lon) {
        this.hubName = hubName;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public void delete() {
        this.isDeleted = true;
    }

}
