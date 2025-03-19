package com.sparta.eureka.hub.application.service;

import com.sparta.eureka.hub.application.dto.HubDto;
import com.sparta.eureka.hub.domain.entity.Hub;
import com.sparta.eureka.hub.domain.repository.HubRepository;
import com.sparta.eureka.hub.infrastructure.geocoding.Coordinates;
import com.sparta.eureka.hub.infrastructure.geocoding.GeocodingService;
import com.sparta.eureka.hub.infrastructure.mapper.HubMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubDataService {
    private final HubRepository hubRepository;
    private final HubMapper hubMapper;
    private final GeocodingService geocodingService;

    public void createHubs(List<HubDto.createDto> requests) {
        List<Hub> hubs = requests.stream()
                .map(hubMapper::createDtoToHub)
                .collect(Collectors.toList());

        hubRepository.saveAll(hubs);
    }

    public void updateAllHubCoordinates() {
        List<Hub> hubs = hubRepository.findAll();

        for (Hub hub : hubs) {
            if (hub.getLat() == null || hub.getLon() == null) {
                Coordinates coordinates = geocodingService.getCoordinatesFromAddress(hub.getAddress());

                hub.latAndLon(
                        BigDecimal.valueOf(coordinates.getLat()),
                        BigDecimal.valueOf(coordinates.getLon())
                );

                hubRepository.save(hub);
            }
        }
    }

    @PostConstruct
    public void initData() {
        // 초기 데이터 생성
        List<HubDto.createDto> hubDtos = Arrays.asList(
                new HubDto.createDto("서울특별시 센터", "서울특별시 송파구 송파대로 55"),
                new HubDto.createDto("경기 북부 센터", "경기도 고양시 덕양구 권율대로 570"),
                new HubDto.createDto("경기 남부 센터", "경기도 이천시 덕평로 257-21"),
                new HubDto.createDto("부산광역시 센터", "부산 동구 중앙대로 206"),
                new HubDto.createDto("인천광역시 센터", "인천 남동구 정각로 29"),
                new HubDto.createDto("광주광역시 센터", "광주 서구 내방로 111"),
                new HubDto.createDto("대전광역시 센터", "대전 서구 둔산로 100"),
                new HubDto.createDto("울산광역시 센터", "울산 남구 중앙로 201"),
                new HubDto.createDto("세종특별자치시 센터", "세종특별자치시 한누리대로 2130"),
                new HubDto.createDto("강원특별자치도 센터", "강원특별자치도 춘천시 중앙로 1"),
                new HubDto.createDto("충청북도 센터", "충북 청주시 상당구 상당로 82"),
                new HubDto.createDto("충청남도 센터", "충남 홍성군 홍북읍 충남대로 21"),
                new HubDto.createDto("전북특별자치도 센터", "전북특별자치도 전주시 완산구 효자로 225"),
                new HubDto.createDto("전라남도 센터", "전남 무안군 삼향읍 오룡길 1"),
                new HubDto.createDto("대구광역시 센터", "대구광역시 북구 침산1동 821"),
                new HubDto.createDto("경상북도 센터", "경상북도 성주군 선남면 동암리 429"),
                new HubDto.createDto("경상남도 센터", "경상남도 창원시 진해구 신항7로 132")
        );

        for (HubDto.createDto hubDto : hubDtos) {
            if (!hubRepository.existsByHubName(hubDto.getHubName())) {
                createHubs(Collections.singletonList(hubDto));
            }
        }

        updateAllHubCoordinates();
    }
}
