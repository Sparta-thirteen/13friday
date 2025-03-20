package com.sparta.eureka.hub.infrastructure.geocoding;

import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/search";

    public Coordinates getCoordinatesFromAddress(String address) {
        // Nominatim API URL 구성
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", address)  //
                .queryParam("format", "json")  // JSON 형식으로 응답받기
                .queryParam("addressdetails", 1)  // 주소 세부 정보 포함
                .queryParam("limit", 1)  // 최대 1개의 결과만 반환
                .build()
                .toString();

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return extractCoordinatesFromJson(response);
    }

    private Coordinates extractCoordinatesFromJson(String jsonResponse) {
        try {
            JSONArray jsonArray = new org.json.JSONArray(jsonResponse);
            if (!jsonArray.isEmpty()) {
                org.json.JSONObject firstResult = jsonArray.getJSONObject(0);
                double lat = firstResult.getDouble("lat");
                double lon = firstResult.getDouble("lon");
                return new Coordinates(lat, lon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Coordinates(0.0, 0.0);
    }

}
