package com.sparta.eureka.hub.presentation.controller;

import com.sparta.eureka.hub.application.dto.hubRoute.HubRouteDto;
import com.sparta.eureka.hub.application.service.HubRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub-route")
public class HubRouteController {
    private final HubRouteService hubRouteService;

    @PostMapping
    public ResponseEntity<HubRouteDto.ResponseDto> createHubRoute(@RequestBody HubRouteDto.CreateDto request) {
        HubRouteDto.ResponseDto response = hubRouteService.createHubRoute(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
