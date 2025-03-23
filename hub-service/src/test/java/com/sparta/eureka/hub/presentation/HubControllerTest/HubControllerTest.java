package com.sparta.eureka.hub.presentation.HubControllerTest;

import com.sparta.eureka.hub.application.dto.hub.HubDto;
import com.sparta.eureka.hub.application.service.HubService;
import com.sparta.eureka.hub.presentation.controller.HubController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class HubControllerTest {

    private HubService hubService;
    private HubController hubController;

    @BeforeEach
    void setUp() {
        hubService = Mockito.mock(HubService.class);
        hubController = new HubController(hubService);
    }

    @Test
    void createHubTest() {
        //given
        String role = "MASTER";
        HubDto.CreateDto request = new HubDto.CreateDto(
                "서울특별시 센터",
                "서울특별시 송파구 송파대로 55"
        );
        HubDto.ResponseDto response = new HubDto.ResponseDto(
                UUID.randomUUID(),
                "서울특별시 센터",
                "서울특별시 송파구 송파대로 55",
                new BigDecimal("37.4759"),
                new BigDecimal("127.1233")
        );

        //when
        when(hubService.createHub(role, request)).thenReturn(response);
        ResponseEntity<HubDto.ResponseDto> result = hubController.createHub(role, request);

        //then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void updateHub() {
        //given
        String role = "MASTER";
        UUID hubId = UUID.randomUUID();
        HubDto.UpdateDto request = new HubDto.UpdateDto(
                "경기 북부 센터",
                "경기도 고양시 덕양구 권율대로 570"
        );
        HubDto.ResponseDto response = new HubDto.ResponseDto(
                UUID.randomUUID(),
                "경기 북부 센터",
                "경기도 고양시 덕양구 권율대로 570",
                new BigDecimal("37.64054"),
                new BigDecimal("126.8737")
        );

        //when
        when(hubService.updateHub(role, hubId, request)).thenReturn(response);
        ResponseEntity<HubDto.ResponseDto> result = hubController.updateHub(role, hubId, request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getHubs() {
        //given
        HubDto.ResponseDto hub1 = new HubDto.ResponseDto(
                UUID.randomUUID(),
                "서울특별시 센터",
                "서울특별시 송파구 송파대로 55",
                new BigDecimal("37.4759"),
                new BigDecimal("127.1233")
        );
        HubDto.ResponseDto hub2 = new HubDto.ResponseDto(
                UUID.randomUUID(),
                "경기 북부 센터",
                "경기도 고양시 덕양구 권율대로 570",
                new BigDecimal("37.64054"),
                new BigDecimal("126.8737")
        );

        List<HubDto.ResponseDto> hubList = Arrays.asList(hub1, hub2);
        Page<HubDto.ResponseDto> page = new PageImpl<>(hubList);

        //when
        when(hubService.getHubs(1, 10)).thenReturn(page);
        ResponseEntity<Page<HubDto.ResponseDto>> result = hubController.getHubs(1, 10);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(page, result.getBody());
    }

    @Test
    void getHub() {
        //given
        UUID hubId = UUID.randomUUID();
        HubDto.ResponseDto response = new HubDto.ResponseDto(
                UUID.randomUUID(),
                "서울특별시 센터",
                "서울특별시 송파구 송파대로 55",
                new BigDecimal("37.4759"),
                new BigDecimal("127.1233")
        );

        //when
        when(hubService.getHub(hubId)).thenReturn(response);
        ResponseEntity<HubDto.ResponseDto> result = hubController.getHub(hubId);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

}