package com.sparta.eureka.hub.infrastructure.geocoding;

import lombok.Getter;

@Getter
public class Coordinates {
    private double lat;
    private double lon;

    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

}
