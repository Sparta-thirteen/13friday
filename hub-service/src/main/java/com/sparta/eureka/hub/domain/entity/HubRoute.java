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

@Builder
@Getter
@Entity
@Table(name = "p_hubRoute")
@NoArgsConstructor
@AllArgsConstructor
public class HubRoute extends Auditable {
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

    @Column(precision = 9, scale = 2)
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

    public void update(Hub departHub, Hub arriveHub) {
        this.departHub = departHub;
        this.arriveHub =arriveHub;
    }

    public void delete(Long userId) {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeleted(true);
        this.setDeletedBy(userId);
    }
}
