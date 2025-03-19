package com.sparta.eureka.hub.presentation.controller;

import com.sparta.eureka.hub.application.dto.HubDto;
import com.sparta.eureka.hub.application.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hub")
public class HubController {
    private final HubService hubService;

    @PostMapping
    public ResponseEntity<HubDto.responseDto> createHub(@RequestBody HubDto.createDto request) {
        HubDto.responseDto response = hubService.createHub(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{hub_id}")
    public ResponseEntity<HubDto.responseDto> updateHub(@PathVariable("hub_id") UUID hubId,
                                                        @RequestBody HubDto.updateDto request) {
        HubDto.responseDto response = hubService.updateHub(hubId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<HubDto.responseDto>> getHubs(@RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "1") int page) {

        Page<HubDto.responseDto> response = hubService.getHubs(size, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<HubDto.responseDto> getHub(@PathVariable("hubId") UUID hubId) {
        HubDto.responseDto response = hubService.getHub(hubId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{hubId}/updateCoordinates")
    public ResponseEntity<Void> updateCoordinates(@PathVariable UUID hubId) {
        hubService.updateHubCoordinates(hubId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<HubDto.responseDto> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
