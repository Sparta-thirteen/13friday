package com.sparta.eureka.hub.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Entity
@Table(name = "p_hubRoute")
@NoArgsConstructor
@AllArgsConstructor
public class HubRoute {
    @Id
    @GeneratedValue
    private UUID hubRouteId;

    @ManyToOne
    @JoinColumn(name = "depart_hub_id")
    private Hub departHub;

    @ManyToOne
    @JoinColumn(name = "arrive_hub_id")
    private Hub arriveHub;

    private Long estimatedTime;
    private BigDecimal distance;

    public static HubRoute create(Hub departHub, Hub arriveHub) {
        return builder()
                .departHub(departHub)
                .arriveHub(arriveHub)
                .build();
    }

    public void setEstimatedTimeAndDistance(Long estimatedTime,  BigDecimal distance) {
        this.estimatedTime = estimatedTime;
        this.distance = distance;
    }
}
