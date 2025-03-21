package com.sparta.eureka.hub.presentation.controller;

import com.sparta.eureka.hub.application.dto.hub.HubDto;
import com.sparta.eureka.hub.application.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub")
public class HubController {
    private final HubService hubService;

    @PostMapping
    public ResponseEntity<HubDto.ResponseDto> createHub(@RequestBody HubDto.CreateDto request) {
        HubDto.ResponseDto response = hubService.createHub(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{hub_id}")
    public ResponseEntity<HubDto.ResponseDto> updateHub(@PathVariable("hub_id") UUID hubId,
                                                        @RequestBody HubDto.UpdateDto request) {
        HubDto.ResponseDto response = hubService.updateHub(hubId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<HubDto.ResponseDto>> getHubs(@RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "1") int page) {

        Page<HubDto.ResponseDto> response = hubService.getHubs(size, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<HubDto.ResponseDto> getHub(@PathVariable("hubId") UUID hubId) {
        HubDto.ResponseDto response = hubService.getHub(hubId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HubDto.ResponseDto>> searchHubs(@RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam String keyword,
                                                               @RequestParam(defaultValue = "true") boolean isDesc) {
        Page<HubDto.ResponseDto> response = hubService.searchHubs(size, page, keyword, isDesc);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{hubId}/updateCoordinates")
    public ResponseEntity<Void> updateCoordinates(@PathVariable UUID hubId) {
        hubService.updateHubCoordinates(hubId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<HubDto.ResponseDto> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
