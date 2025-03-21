package com.sparta.eureka.hub.presentation.controller;

import com.sparta.eureka.hub.application.dto.hubRoute.HubRouteDto;
import com.sparta.eureka.hub.application.service.HubRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PatchMapping("/{hubRouteId}")
    public ResponseEntity<HubRouteDto.ResponseDto> updateHubRoute(@PathVariable UUID hubRouteId,
                                                                  @RequestBody HubRouteDto.UpdateDto request) {
        HubRouteDto.ResponseDto response = hubRouteService.updateHubRoute(hubRouteId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<HubRouteDto.ResponseDto>> getHubRoutes(@RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "1") int page) {
        Page<HubRouteDto.ResponseDto> response = hubRouteService.getHubRoutes(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{hubRouteId}")
    public ResponseEntity<HubRouteDto.ResponseDto> getHubRoute(@PathVariable UUID hubRouteId) {
        HubRouteDto.ResponseDto response = hubRouteService.getHubRoute(hubRouteId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{hubRouteId}")
    public ResponseEntity<HubRouteDto.ResponseDto> deleteHubRoute(@PathVariable UUID hubRouteId) {
        hubRouteService.deleteHubRoute(hubRouteId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
